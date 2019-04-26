package com.anbang.p2p.web.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.constants.CommonRecordState;
import com.anbang.p2p.constants.PayType;
import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.q.dao.UserDboDao;
import com.anbang.p2p.cqrs.q.dbo.*;
import com.anbang.p2p.cqrs.q.service.RefundInfoService;
import com.anbang.p2p.plan.bean.*;
import com.anbang.p2p.plan.dao.LeaveWordDao;
import com.anbang.p2p.plan.service.RiskService;
import com.anbang.p2p.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;
import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.service.OrderQueryService;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.plan.service.BaseRateService;
import com.anbang.p2p.web.vo.CommonVO;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	private UserAuthQueryService userAuthQueryService;

	@Autowired
	private OrderCmdService orderCmdService;

	@Autowired
	private OrderQueryService orderQueryService;

	@Autowired
	private BaseRateService baseRateService;

	@Autowired
	private UserDboDao userDboDao;

	@Autowired
	private RiskService riskService;

	@Autowired
	private RefundInfoService refundInfoService;

	@Autowired
	private LeaveWordDao leaveWordDao;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	/**
	 * 查询提示
	 */
	@RequestMapping("/queryOrderTip")
	public CommonVO queryOrderTip(String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}
		LoanOrder loanOrder = orderQueryService.findLastOrderByUserId(userId);
		if (loanOrder == null) {
			return CommonVOUtil.success("init");
		}

		if (OrderState.refund.equals(loanOrder.getState())) {
			long maxLimitTime = loanOrder.getMaxLimitTime();
			long nowTIme = System.currentTimeMillis();
			double flag = TimeUtils.repayTime(maxLimitTime, nowTIme);
			if (flag > 1 && flag < 2) {
				return CommonVOUtil.success(Notification.getMap().get("repayDay").toString());
			}
			if (flag > 0 && flag < 1) {
				return CommonVOUtil.success(Notification.getMap().get("repayFront").toString());
			}
		}
		if (OrderState.clean.equals(loanOrder.getState())) {
			return CommonVOUtil.success(Notification.getMap().get("repaySuccess").toString());
		}
		if (OrderState.overdue.equals(loanOrder.getState())) {
			double flag = TimeUtils.repayTime(System.currentTimeMillis(), loanOrder.getMaxLimitTime());
			if (flag > 15) {
				return CommonVOUtil.success(Notification.getMap().get("beyond15").toString());
			}
			if (flag > 7) {
				return CommonVOUtil.success(Notification.getMap().get("beyond7").toString());
			}
			if (flag > 3) {
				return CommonVOUtil.success(Notification.getMap().get("beyond3").toString());
			}
			if (flag > 1) {
				return CommonVOUtil.success(Notification.getMap().get("beyond1").toString());
			}
		}
		return CommonVOUtil.success("init");
	}

	/**
	 * 申请卡密
	 */
	@RequestMapping("/createorder")
	public CommonVO createOrder(String token, String contractId, HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		UserDbo user = userAuthQueryService.findUserDboByUserId(userId);
		// TODO 身份认证
		UserBaseInfo baseInfo = userAuthQueryService.findUserBaseInfoByUserId(userId);
		if (baseInfo == null) {
			vo.setSuccess(false);
			vo.setMsg("not finish VerifyTest baseInfo");
			return vo;
		}

		// TODO 紧急联系人
		UserContacts contacts = userAuthQueryService.findUserContactsByUserId(userId);
		if (contacts == null) {
			vo.setSuccess(false);
			vo.setMsg("not finish VerifyTest contacts");
			return vo;
		}

//		// TODO 绑定银行卡
		long bankCardCount = userAuthQueryService.getAmountByUserId(userId);
		if (bankCardCount == 0) {
			vo.setSuccess(false);
			vo.setMsg("invalid cardId");
			return vo;
		}

	    // TODO 绑定支付宝
		UserDbo userDbo = userDboDao.findById(userId);
		if (userDbo == null || userDbo.getAlipayInfo() == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid alipay");
			return vo;
		}

		MobileVerify mobileVerify = userAuthQueryService.getMobileVerify(userId);
		if (mobileVerify == null || !CommonRecordState.SUCCESS.equals(mobileVerify.getState())) {
			vo.setSuccess(false);
			vo.setMsg("lack mobile verify");
			return vo;
		}

//		// TODO 合同认证
		OrderContract contract = orderQueryService.findOrderContractById(contractId);
		if (contract == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid contract");
			return vo;
		}


		double amount = BaseLoan.baseLimit;
		double service_charge = BaseLoan.service_charge;
		long freeTimeOfInterest = BaseLoan.freeTimeOfInterest;
		long overdue = BaseLoan.overdue;
		double overdue_rate = BaseLoan.overdue_rate;
		double expand_charge = BaseLoan.expand_charge;
		UserBaseLoan loan = baseRateService.findUserBaseLoanByUserId(userId);
		if (loan != null) {
			amount = loan.getBaseLimit();
			service_charge = loan.getService_charge();
			freeTimeOfInterest = loan.getFreeTimeOfInterest();
			overdue = loan.getOverdue();
			overdue_rate = loan.getOverdue_rate();
		}
		try {
			// 生成卡密
			int dayNum = (int) freeTimeOfInterest  / 1000 / 60 / 60 / 24;	// 借款天数
			OrderValueObject orderValueObject = orderCmdService.createOrder(userId, PayType.alipay, userDbo.getAlipayInfo().getAccount(), amount,
					service_charge, freeTimeOfInterest, overdue, overdue_rate, dayNum, contractId, expand_charge,
					System.currentTimeMillis());

			// IP记录
			String loginIp = IPUtil.getRealIp(request);
			String ipAddress = IPAddressUtil.getIPAddress(loginIp);
			LoanOrder loanOrder = orderQueryService.saveLoanOrder(orderValueObject, user, contract, baseInfo, loginIp, ipAddress);

			//风控
			checkOrderByFengkong(loanOrder);
			userDboDao.updateCountAndState(userId, user.getOrderCount() + 1, null, OrderState.wait.name());
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		return vo;
	}

	@RequestMapping("/getRepayUrl")
	public CommonVO getRepayUrl(String token, String payType) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}

		if (!"alipay".equals(payType) && !"wechat".equals(payType)) {
			return CommonVOUtil.error("支付方式错误");
		}

		LoanOrder loanOrder = orderQueryService.findLastOrderByUserId(userId);
		if (loanOrder == null) {
			return CommonVOUtil.error("OrderNotFoundException");
		}

		try {
			double amount = orderQueryService.queryRefundAmount(userId, System.currentTimeMillis());
			BigDecimal b = new BigDecimal(amount);
			amount = b.setScale(2, BigDecimal.ROUND_UP).doubleValue();

			RefundInfo refundInfo = new RefundInfo(loanOrder, payType, amount);
			refundInfo.setStatus(CommonRecordState.INIT);
			refundInfoService.save(refundInfo);
			Map data = AgentIncome.income(amount, refundInfo.getId(), payType);
			return CommonVOUtil.success(data, "success");

		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return CommonVOUtil.error("OrderNotFoundException");
		} catch (IllegalOperationException e) {
			e.printStackTrace();
		}
		return CommonVOUtil.systemException();

	}

	/**
	 * 查询还款金额
	 */
	@RequestMapping("/getRefundAmount")
	public CommonVO getRefundAmount(String token) {
		CommonVO vo = new CommonVO();
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		LoanOrder loanOrder = orderQueryService.findLastOrderByUserId(userId);
		if (loanOrder == null) {
			vo.setSuccess(false);
			vo.setMsg("OrderNotFoundException");
			return vo;
		}
		if (!loanOrder.getState().equals(OrderState.refund)) {
			vo.setSuccess(false);
			vo.setMsg("IllegalOperationException");
			return vo;
		}
		if (System.currentTimeMillis() < loanOrder.getDeliverTime()) {
			vo.setSuccess(false);
			vo.setMsg("IllegalOperationException");
			return vo;
		}

		try {
			Map data = new HashMap();

			if (!OrderState.refund.equals(loanOrder.getState()) && !OrderState.overdue.equals(loanOrder.getState()) &&
					!OrderState.collection.equals(loanOrder.getState())){
				data.put("amount", 0);
				data.put("status", loanOrder.getState());
				return CommonVOUtil.success(data, "不需要还款");
			}

			double amount = loanOrder.getShouldRepayAmount();
			data.put("amount", amount);
			data.put("status", loanOrder.getState());
			data.put("orderAmount", loanOrder.getAmount());
			data.put("maxLimitTime", loanOrder.getMaxLimitTime());
			data.put("overdueDay", loanOrder.getOverdueDay());
			return CommonVOUtil.success(data,"success");
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
	}

	/**
	 * 新增留言
	 */
	@RequestMapping("/addLeaveWord")
	public CommonVO addLeaveWord(String token, LeaveWord leaveWord) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.error("invalid token");
		}

		if (StringUtils.isNotBlank(leaveWord.getMsg())) {
			return CommonVOUtil.invalidParam();
		}

		leaveWord.setUserId(userId);
		leaveWord.setCreateTime(System.currentTimeMillis());
		leaveWordDao.save(leaveWord);
		return CommonVOUtil.success("success");
	}

	/**
	 * 风控审核
	 */
	private void checkOrderByFengkong(LoanOrder loanOrder) {
		executorService.submit(() -> {
			try {
				RiskInfo riskInfo = new RiskInfo();
				String id = UUID.randomUUID().toString().replace("-","");
				riskInfo.setId(id);
				riskInfo.setUserId(loanOrder.getUserId());
				riskInfo.setCreateTime(System.currentTimeMillis());

				// 第三方风控，有盾用户画像
				JSONObject object = RiskUtil.getRiskInfo(loanOrder.getIDcard(), riskInfo.getId());
				String riskStr = JSON.toJSONString(object);

				riskInfo.setRiskInfo(riskStr);
				riskService.save(riskInfo);


				OrderValueObject orderValueObject = orderCmdService
						.changeOrderStateToCheck_by_admin(loanOrder.getUserId());
				orderQueryService.updateLoanOrder(orderValueObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
