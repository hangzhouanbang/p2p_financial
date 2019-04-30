package com.anbang.p2p.web.controller;

import java.util.HashMap;
import java.util.Map;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.q.dbo.*;
import com.anbang.p2p.cqrs.q.service.OrderQueryService;
import com.anbang.p2p.plan.bean.BaseLoan;
import com.anbang.p2p.plan.bean.Notification;
import com.anbang.p2p.plan.bean.OrgInfo;
import com.anbang.p2p.plan.bean.UserBaseLoan;
import com.anbang.p2p.plan.service.BaseRateService;
import com.anbang.p2p.util.AmountToUpper;
import com.anbang.p2p.util.CalAmountUtil;
import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.util.TimeUtils;
import com.anbang.p2p.web.vo.LoanOrderQueryVO;
import com.highto.framework.web.page.ListPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.web.vo.CommonVO;

@CrossOrigin
@RestController
@RequestMapping("/userinfo")
public class UserInfoController {

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	private UserAuthQueryService userAuthQueryService;

	@Autowired
	private BaseRateService baseRateService;

	@Autowired
	private OrderQueryService orderQueryService;


	/**
	 * 用户卡密
	 */
	@RequestMapping("/getOrder")
	public CommonVO getOrder(String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}

		LoanOrder loanOrder = orderQueryService.findLastOrderByUserId(userId);
		if (loanOrder != null) {
			loanOrder.setContractPath(null);
		}
		return CommonVOUtil.success(loanOrder, "success");
	}

	/**
	 * 用户卡密list
	 */
	@RequestMapping("/listOrder")
	public CommonVO listOrder(String token, int page, int size) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}

		LoanOrderQueryVO query = new LoanOrderQueryVO();
		query.setUserId(userId);
		ListPage listPage = orderQueryService.findLoanOrder(page, size, query);
		return CommonVOUtil.success(listPage, "success");
	}

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
			return CommonVOUtil.error("init");
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
			return CommonVOUtil.success(Notification.getMap().get("applySuccess").toString());
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
		return CommonVOUtil.error("init");
	}

	/**
	 * 用户信息
	 */
	@RequestMapping("/query_user")
	public CommonVO queryUser(String token) {
		CommonVO vo = new CommonVO();
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		UserDbo user = userAuthQueryService.findUserDboByUserId(userId);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("user", user);
		return vo;
	}

	/**
	 * 修改用户头像和昵称
	 */
	@RequestMapping("/update_user")
	public CommonVO updateUser(String token, String nickname, String headimgurl) {
		CommonVO vo = new CommonVO();
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		userAuthQueryService.updateNicknameAndHeadimgurlById(userId, nickname, headimgurl);
		return vo;
	}

	/**
	 * 实名认证信息
	 */
	@RequestMapping("/query_baseinfo")
	public CommonVO queryBaseInfo(String token) {
		CommonVO vo = new CommonVO();
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		UserBaseInfo baseInfo = userAuthQueryService.findUserBaseInfoByUserId(userId);
		if (baseInfo != null && !baseInfo.finishUserVerify()) {
			vo.setSuccess(false);
			vo.setMsg("not finish VerifyTest");
			return vo;
		}
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("baseInfo", baseInfo);
		return vo;
	}

	/**
	 * 运营商认证信息
	 */
	@RequestMapping("/query_agentinfo")
	public CommonVO queryAgentInfo(String token) {
		CommonVO vo = new CommonVO();
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		UserAgentInfo agentInfo = userAuthQueryService.findUserAgentInfoByUserId(userId);
		if (agentInfo != null && agentInfo.finishAgentVerify()) {
			vo.setSuccess(false);
			vo.setMsg("not finish VerifyTest");
			return vo;
		}
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("agentInfo", agentInfo);
		return vo;
	}

	/**
	 * 紧急联系人信息
	 */
	@RequestMapping("/query_contacts")
	public CommonVO queryContacts(String token) {
		CommonVO vo = new CommonVO();
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		UserContacts contacts = userAuthQueryService.findUserContactsByUserId(userId);
		if (contacts != null && contacts.finishContactsVerify()) {
			vo.setSuccess(false);
			vo.setMsg("not finish VerifyTest");
			return vo;
		}
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("contacts", contacts);
		return vo;
	}

	/**
	 * 用户信用信息
	 */
	@RequestMapping("/query_credit")
	public CommonVO queryCredit(String token) {
		CommonVO vo = new CommonVO();
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		UserCreditInfo creditInfo = userAuthQueryService.findUserCreditInfoByUserId(userId);
		if (creditInfo != null && creditInfo.finishCreditVerify()) {
			vo.setSuccess(false);
			vo.setMsg("not finish VerifyTest");
			return vo;
		}
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("creditInfo", creditInfo);
		return vo;
	}

	/**
	 * 查询玩家基本设置
	 */
	@RequestMapping("/user_baseloan_query")
	public CommonVO queryUserBaseLoan(String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}

		UserBaseLoan loan = baseRateService.findUserBaseLoanByUserId(userId);
		Map data = new HashMap();
		if (loan == null) {
			data.put("id", userId);
			data.put("baseLimit", BaseLoan.baseLimit);
			data.put("service_charge", BaseLoan.service_charge);
			data.put("expand_charge", BaseLoan.expand_charge);
			data.put("overdue", BaseLoan.overdue / 24 / 60 / 60 / 1000);
			data.put("freeTimeOfInterest", BaseLoan.freeTimeOfInterest / 24 / 60 / 60 / 1000);
			data.put("overdue_rate", BaseLoan.overdue_rate * 1000);
			data.put("day_amount", CalAmountUtil.calDayAmount(BaseLoan.baseLimit, BaseLoan.overdue_rate));
			data.put("repayTime", TimeUtils.getStringHr(System.currentTimeMillis() + BaseLoan.freeTimeOfInterest));
			return CommonVOUtil.success(data, "success");
		} else {
			data.put("id", userId);
			data.put("baseLimit", loan.getBaseLimit());
			data.put("service_charge", loan.getService_charge());
			data.put("expand_charge", loan.getExpand_charge());
			data.put("overdue", loan.getOverdue() / 24 / 60 / 60 / 1000);
			data.put("freeTimeOfInterest", loan.getFreeTimeOfInterest() / 24 / 60 / 60 / 1000);
			data.put("overdue_rate", loan.getOverdue_rate() * 1000);
			data.put("day_amount", CalAmountUtil.calDayAmount(loan.getBaseLimit(), loan.getOverdue_rate()));
			data.put("repayTime", TimeUtils.getStringHr(System.currentTimeMillis() + loan.getFreeTimeOfInterest()));
			return CommonVOUtil.success(data, "success");
		}
	}

	/**
	 * 查询玩家合同信息
	 */
	@RequestMapping("/queryContract")
	public CommonVO queryContract(String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}

		UserDbo userDbo = userAuthQueryService.findUserDboByUserId(userId);
		if (userDbo == null) {
			return CommonVOUtil.error("未进行身份认证");
		}

		Map data = new HashMap();
		data.put("id_card", userDbo.getIDcard());
		data.put("realName", userDbo.getRealName());

//		OrgInfo orgInfo = userAuthQueryService.getOrgInfo("001");
//		if (orgInfo != null) {
//			data.put("org_name", orgInfo.getOrgName());
//			data.put("org_phone", orgInfo.getPhone());
//		}
		data.put("org_name", "快鹿小贷");
		data.put("org_phone", "000000");

		UserBaseLoan loan = baseRateService.findUserBaseLoanByUserId(userId);
		if (loan == null) {
			data.put("id", userId);
			data.put("baseLimit", BaseLoan.baseLimit);
			data.put("service_charge", BaseLoan.service_charge);
			data.put("expand_charge", BaseLoan.expand_charge);
			data.put("overdue", BaseLoan.overdue / 24 / 60 / 60 / 1000);
			data.put("freeTimeOfInterest", BaseLoan.freeTimeOfInterest / 24 / 60 / 60 / 1000);
			data.put("overdue_rate", BaseLoan.overdue_rate * 1000);

			data.put("capital", AmountToUpper.number2CNMontrayUnit(BaseLoan.baseLimit));
			data.put("start", TimeUtils.getStringDate(System.currentTimeMillis()));
			data.put("end", TimeUtils.getStringDate(System.currentTimeMillis() + BaseLoan.freeTimeOfInterest));
			return CommonVOUtil.success(data, "success");
		} else {
			data.put("id", userId);
			data.put("baseLimit", loan.getBaseLimit());
			data.put("service_charge", loan.getService_charge());
			data.put("expand_charge", loan.getExpand_charge());
			data.put("overdue", loan.getOverdue() / 24 / 60 / 60 / 1000);
			data.put("freeTimeOfInterest", loan.getFreeTimeOfInterest() / 24 / 60 / 60 / 1000);
			data.put("overdue_rate", loan.getOverdue_rate() * 1000);

			data.put("capital", AmountToUpper.number2CNMontrayUnit(loan.getBaseLimit()));
			data.put("start", TimeUtils.getStringDate(System.currentTimeMillis()));
			data.put("end", TimeUtils.getStringDate(System.currentTimeMillis() + loan.getFreeTimeOfInterest()));
			return CommonVOUtil.success(data, "success");
		}
	}

	/**
	 * 查询玩家合同信息
	 */
	@RequestMapping("/getOrgInfo")
	public CommonVO getOrgInfo(String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}

		OrgInfo orgInfo = userAuthQueryService.getOrgInfo("001");
		return CommonVOUtil.success(orgInfo, "success");
	}
}
