package com.anbang.p2p.web.controller;

import com.anbang.p2p.util.CommonVOUtil;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.dbo.UserBankCardInfo;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.web.vo.CommonVO;

@RestController
@RequestMapping("/bankcard")
public class BankCardController {

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	private UserAuthQueryService userAuthQueryService;

	/**
	 * 绑定银行卡
	 */
	@RequestMapping("/bind")
	public CommonVO bindBankCard(UserBankCardInfo info, String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}
		if (StringUtil.isBlank(info.getBank()) || StringUtil.isBlank(info.getBankCardNo())) {
			return CommonVOUtil.invalidParam();
		}
		// TODO 查询银行卡
		info.setUserId(userId);
		userAuthQueryService.saveBankCardInfo(info);
		return CommonVOUtil.success("success");
	}
}
