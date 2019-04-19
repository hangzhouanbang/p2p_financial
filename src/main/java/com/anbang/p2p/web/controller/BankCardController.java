package com.anbang.p2p.web.controller;

import com.anbang.p2p.cqrs.q.dao.UserDboDao;
import com.anbang.p2p.cqrs.q.dbo.AlipayInfo;
import com.anbang.p2p.cqrs.q.dbo.UserDbo;
import com.anbang.p2p.util.CommonVOUtil;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.dbo.UserBankCardInfo;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.web.vo.CommonVO;

@CrossOrigin
@RestController
@RequestMapping("/bankcard")
public class BankCardController {

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	private UserAuthQueryService userAuthQueryService;

	@Autowired
	private UserDboDao userDboDao;

	/**
	 * 绑定银行卡
	 */
	@RequestMapping("/bind")
	public CommonVO bindBankCard(UserBankCardInfo info, String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}
		if (StringUtil.isBlank(info.getPhone()) || StringUtil.isBlank(info.getBankCardNo())) {
			return CommonVOUtil.invalidParam();
		}

		// todo 只绑定一张卡
		info.setId(userId);
		info.setUserId(userId);
		userAuthQueryService.saveBankCardInfo(info);
		return CommonVOUtil.success("success");
	}

	/**
	 * 绑定支付宝
	 */
	@RequestMapping("/bindAlipay")
	public CommonVO bindAlipay(String token, AlipayInfo alipayInfo) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}

		alipayInfo.setAccount(alipayInfo.getAccount().trim());
		alipayInfo.setName(alipayInfo.getName().trim());
		if (StringUtil.isBlank(alipayInfo.getAccount()) || StringUtil.isBlank(alipayInfo.getName())) {
			return CommonVOUtil.invalidParam();
		}

		UserDbo userDbo = userDboDao.findById(userId);
		if (userDbo == null) {
			return CommonVOUtil.systemException();
		}

		alipayInfo.setUpdateTime(System.currentTimeMillis());
		userDbo.setAlipayInfo(alipayInfo);
		return CommonVOUtil.success("success");
	}
}
