package com.anbang.p2p.web.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;
import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.dbo.LoanOrder;
import com.anbang.p2p.cqrs.q.dbo.OrderContract;
import com.anbang.p2p.cqrs.q.dbo.UserAgentInfo;
import com.anbang.p2p.cqrs.q.dbo.UserBankCardInfo;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserContacts;
import com.anbang.p2p.cqrs.q.dbo.UserCreditInfo;
import com.anbang.p2p.cqrs.q.dbo.UserDbo;
import com.anbang.p2p.cqrs.q.service.OrderQueryService;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.plan.bean.BaseLoan;
import com.anbang.p2p.plan.bean.BaseRateOfInterest;
import com.anbang.p2p.plan.bean.UserBaseLoan;
import com.anbang.p2p.plan.bean.UserBaseRateOfInterest;
import com.anbang.p2p.plan.service.BaseRateService;
import com.anbang.p2p.web.vo.CommonVO;

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

	private ExecutorService executorService = Executors.newCachedThreadPool();

	/**
	 * 申请卡密
	 */
	@RequestMapping("/createorder")
	public CommonVO createOrder(String token, String cardId, String contractId, int dayNum) {
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
			vo.setMsg("not finish verify baseInfo");
			return vo;
		}
		// TODO 运营商认证
		UserAgentInfo agentInfo = userAuthQueryService.findUserAgentInfoByUserId(userId);
		if (agentInfo == null) {
			vo.setSuccess(false);
			vo.setMsg("not finish verify agentInfo");
			return vo;
		}
		// TODO 紧急联系人
		UserContacts contacts = userAuthQueryService.findUserContactsByUserId(userId);
		if (contacts == null) {
			vo.setSuccess(false);
			vo.setMsg("not finish verify contacts");
			return vo;
		}
		// TODO 芝麻认证
		UserCreditInfo creditInfo = userAuthQueryService.findUserCreditInfoByUserId(userId);
		if (creditInfo == null) {
			vo.setSuccess(false);
			vo.setMsg("not finish verify creditInfo");
			return vo;
		}
		// TODO 绑定银行卡
		UserBankCardInfo cardInfo = userAuthQueryService.findUserBankCardInfoById(cardId);
		if (cardInfo == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid cardId");
			return vo;
		}
		// TODO 合同认证
		OrderContract contract = orderQueryService.findOrderContractById(contractId);
		// if (contract == null) {
		// vo.setSuccess(false);
		// vo.setMsg("invalid contract");
		// return vo;
		// }
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
			OrderValueObject orderValueObject = orderCmdService.createOrder(userId, cardInfo.getBankCardNo(), amount,
					service_charge_rate, freeTimeOfInterest, overdue, overdue_rate, rate, dayNum, contractId,
					System.currentTimeMillis());
			LoanOrder loanOrder = orderQueryService.saveLoanOrder(orderValueObject, user, contract, baseInfo);
			checkOrderByFengkong(loanOrder);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		return vo;
	}

	/**
	 * 还款
	 */
	@RequestMapping("/refund")
	public CommonVO refundOrder(String token, String cardId) {
		CommonVO vo = new CommonVO();
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		UserBankCardInfo cardInfo = userAuthQueryService.findUserBankCardInfoById(cardId);
		if (cardInfo == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid cardId");
			return vo;
		}
		LoanOrder loanOrder = orderQueryService.findLoanOrderByUserIdAndState(userId, OrderState.refund);
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
		// TODO 还款
		try {
			double amount = orderQueryService.queryRefundAmount(userId, System.currentTimeMillis());
			// 还款
			OrderValueObject orderValueObject = orderCmdService.cleanOrder(userId, amount, System.currentTimeMillis());
			orderQueryService.updateLoanOrder(orderValueObject, cardInfo);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		return vo;
	}

	/**
	 * 风控审核
	 */
	private void checkOrderByFengkong(LoanOrder loanOrder) {
		executorService.submit(() -> {
			try {
				// TODO 第三方风控
				OrderValueObject orderValueObject = orderCmdService
						.changeOrderStateToCheck_by_admin(loanOrder.getUserId());
				orderQueryService.updateLoanOrder(orderValueObject, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
