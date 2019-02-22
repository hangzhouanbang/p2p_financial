package com.anbang.p2p.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;
import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.dbo.UserAgentInfo;
import com.anbang.p2p.cqrs.q.dbo.UserBankCardInfo;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserContacts;
import com.anbang.p2p.cqrs.q.dbo.UserCreditInfo;
import com.anbang.p2p.cqrs.q.dbo.UserDbo;
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
	private BaseRateService baseRateService;

	/**
	 * 申请卡密
	 */
	@RequestMapping("/createorder")
	public CommonVO createOrder(String token, String cardId, int dayNum) {
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
		// TODO 运营商认证
		UserAgentInfo agentInfo = userAuthQueryService.findUserAgentInfoByUserId(userId);
		// TODO 紧急联系人
		UserContacts contacts = userAuthQueryService.findUserContactsByUserId(userId);
		// TODO 芝麻认证
		UserCreditInfo creditInfo = userAuthQueryService.findUserCreditInfoByUserId(userId);
		// TODO 绑定银行卡
		UserBankCardInfo cardInfo = userAuthQueryService.findUserBankCardInfoById(cardId);
		if (cardInfo == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid cardId");
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
			OrderValueObject orderValueObject = orderCmdService.createOrder(userId, cardInfo.getBankCardNo(), amount,
					service_charge_rate, dayNum, freeTimeOfInterest, overdue_rate, rate, System.currentTimeMillis());
			// TODO 保存订单
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		return vo;
	}
}
