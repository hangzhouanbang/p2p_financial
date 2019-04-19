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
import com.anbang.p2p.plan.service.RiskService;
import com.anbang.p2p.util.AgentIncome;
import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.util.RiskUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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

import static com.anbang.p2p.util.AgentIncome.income;

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

	private ExecutorService executorService = Executors.newCachedThreadPool();

	/**
	 * 申请卡密
	 */
	@RequestMapping("/createorder")
	public CommonVO createOrder(String token, String contractId, int dayNum) {
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

//		// TODO 合同认证
		OrderContract contract = orderQueryService.findOrderContractById(contractId);
		if (contract == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid contract");
			return vo;
		}

		double amount = BaseLoan.baseLimit;
		double service_charge_rate = BaseLoan.service_charge_rate;
		long freeTimeOfInterest = BaseLoan.freeTimeOfInterest;
		long overdue = BaseLoan.overdue;
		UserBaseLoan loan = baseRateService.findUserBaseLoanByUserId(userId);
		if (loan != null) {
			amount = loan.getBaseLimit();
			service_charge_rate = loan.getService_charge_rate();
			freeTimeOfInterest = loan.getFreeTimeOfInterest();
			overdue = loan.getOverdue();
		}
		try {
			double rate = BaseRateOfInterest.getRateByDayNum(dayNum);
			double overdue_rate = BaseRateOfInterest.overdue_rate;
			UserBaseRateOfInterest userBaseRateOfInterest = baseRateService.findUserBaseRateOfInterestByUserId(userId);
			if (userBaseRateOfInterest != null) {
				rate = userBaseRateOfInterest.getRateByDayNum(dayNum);
				overdue_rate = userBaseRateOfInterest.getOverdue_rate();
			}
			// 生成卡密
			OrderValueObject orderValueObject = orderCmdService.createOrder(userId, PayType.alipay, userDbo.getAlipayInfo().getAccount(), amount,
					service_charge_rate, freeTimeOfInterest, overdue, overdue_rate, rate, dayNum, contractId,
					System.currentTimeMillis());
			LoanOrder loanOrder = orderQueryService.saveLoanOrder(orderValueObject, user, contract, baseInfo);

			//风控
			checkOrderByFengkong(loanOrder);
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

			double amount = orderQueryService.queryRefundAmount(userId, System.currentTimeMillis());
			data.put("amount", amount);
			data.put("status", loanOrder.getState());
			return CommonVOUtil.success(data,"success");
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
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
