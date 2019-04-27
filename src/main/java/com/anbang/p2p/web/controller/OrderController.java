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
import com.anbang.p2p.constants.PaymentType;
import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.q.dao.UserDboDao;
import com.anbang.p2p.cqrs.q.dbo.*;
import com.anbang.p2p.cqrs.q.service.RefundInfoService;
import com.anbang.p2p.plan.bean.*;
import com.anbang.p2p.plan.dao.LeaveWordDao;
import com.anbang.p2p.plan.dao.OrgInfoDao;
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

			//获取风控，保存合同
			riskAndContract(loanOrder);
			userDboDao.updateCountAndState(userId, user.getOrderCount() + 1, null, OrderState.wait.name());

			Map data = new HashMap();
			data.put("realAmount", loanOrder.getRealAmount());
			data.put("payAccount", loanOrder.getPayAccount());
			data.put("realName", loanOrder.getRealName());
			data.put("id", loanOrder.getId());
			return CommonVOUtil.success(data, "success");
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
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
			refundInfo.setPaymentType(PaymentType.repay);
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

	@RequestMapping("/getExpandUrl")
	public CommonVO getExpandUrl(String token, String payType) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}

		if (!"alipay".equals(payType) && !"wechat".equals(payType)) {
			return CommonVOUtil.error("payTypeError");
		}

		LoanOrder order = orderQueryService.findLastOrderByUserId(userId);
		if (order == null) {
			return CommonVOUtil.error("OrderNotFoundException");
		}

		try {
			if (OrderState.refund.equals(order.getState()) || OrderState.overdue.equals(order.getState()) ||
					OrderState.collection.equals(order.getState())) {
				double amount = order.getExpand_charge();
				BigDecimal b = new BigDecimal(amount);
				amount = b.setScale(2, BigDecimal.ROUND_UP).doubleValue();

				RefundInfo refundInfo = new RefundInfo(order, payType, amount);
				refundInfo.setPaymentType(PaymentType.expand);
				refundInfo.setStatus(CommonRecordState.INIT);
				refundInfoService.save(refundInfo);
				Map data = AgentIncome.income(amount, refundInfo.getId(), payType);
				return CommonVOUtil.success(data, "success");
			}

			return CommonVOUtil.error("orderStateError");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CommonVOUtil.systemException();

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
	 * 风控、合同保存
	 */
	private void riskAndContract(LoanOrder loanOrder) {
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

		// 保存合同
		executorService.submit(() -> {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("${realName}", loanOrder.getRealName());
				map.put("${card}", loanOrder.getIDcard());

				OrgInfo orgInfo = userAuthQueryService.getOrgInfo("001");
				if (orgInfo != null) {
					map.put("${org}", orgInfo.getOrgName());
					map.put("${phone}", orgInfo.getPhone());
				}

				map.put("${amount}", String.valueOf(loanOrder.getAmount()));
				map.put("${capital}", AmountToUpper.number2CNMontrayUnit(loanOrder.getAmount()));
				map.put("${rate}", String.valueOf(loanOrder.getOverdue_rate() * 100));
				map.put("${day}", String.valueOf(loanOrder.getFreeTimeOfInterest() / 24 * 60 * 60 * 1000));
				map.put("${start}", TimeUtils.getStringDate(loanOrder.getDeliverTime()));
				map.put("${end}", TimeUtils.getStringDate(loanOrder.getMaxLimitTime()));
				String destPath = WordUtil.PATH + loanOrder.getId() + ".docx";
		 		WordUtil.searchAndReplace(WordUtil.DEMO_PATH, destPath, map);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
