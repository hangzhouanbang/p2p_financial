package com.anbang.p2p.web.controller;

import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.anbang.p2p.conf.VerifyConfig;
import com.anbang.p2p.constants.CommonRecordState;
import com.anbang.p2p.plan.bean.VerifyRecord;
import com.anbang.p2p.plan.service.RiskService;
import com.anbang.p2p.plan.service.VerifyRecordService;
import com.anbang.p2p.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserContacts;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.plan.service.BaseVerifyService;
import com.anbang.p2p.web.vo.CommonVO;
import com.google.gson.Gson;

@CrossOrigin
@RestController
@RequestMapping("/verify")
public class UserVerifyController {

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	private BaseVerifyService baseVerifyService;

	@Autowired
	private UserAuthQueryService userAuthQueryService;

	@Autowired
	private VerifyRecordService verifyRecordService;

	private Gson gson = new Gson();

	/**
	 * 云慧眼加密加签
	 */
	@RequestMapping("/addSign")
	public CommonVO addSign(String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.error("invalid token");
		}

		UserBaseInfo baseInfo = userAuthQueryService.findUserBaseInfoByUserId(userId);
		if (baseInfo != null && baseInfo.finishUserVerify()) {
			return CommonVOUtil.error("finish VerifyTest");
		}

		VerifyRecord record = new VerifyRecord();
		record.setUerId(userId);
		record.setState(CommonRecordState.INIT);
		record.setCauseBy("加密加签");
		record.setCreateTime(System.currentTimeMillis());
		verifyRecordService.save(record);

		String partner_order_id = record.getId();
		String pub_key = VerifyConfig.PUB_KEY;
		String sign_time = TimeUtils.getStringDate(new Date());
		String sign = RiskUtil.getMD5Sign(record.getId(), sign_time);	// 获取签名
		String return_url = VerifyConfig.RETURN_URL;
		String callback_url = VerifyConfig.CALLBACK_URL;

		String params = String.format("partner_order_id=%s&pub_key=%s&sign_time=%s&sign=%s&return_url=%s&callback_url=%s",
				partner_order_id, pub_key, sign_time, sign, return_url, callback_url);
		try {
			String url = RiskUtil.getAESSign(params);
			return CommonVOUtil.success(url,"success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CommonVOUtil.systemException();
	}

	/**
	 * 紧急联系人
	 */
	@RequestMapping("/contacts")
	public CommonVO contacts(String token, UserContacts userContacts) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.error("invalid token");
		}
		if (userContacts == null || UserContacts.isBlank(userContacts)) {
			return CommonVOUtil.error("invalid param");
		}
		if (!Pattern.matches("[0-9]{11}", userContacts.getCommonContactsPhone()) ||
				!Pattern.matches("[0-9]{11}", userContacts.getDirectContactsPhone())) {// 检验手机格式
			return CommonVOUtil.error("invalid phone");
		}



		userContacts.setId(userId);
		userContacts.setUserId(userId);
		userAuthQueryService.saveContacts(userContacts);
		return CommonVOUtil.success("success");
	}

	/**
	 * 紧急联系人
	 */
	@RequestMapping("/getContacts")
	public CommonVO getContacts(String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.error("invalid token");
		}

		UserContacts userContact = userAuthQueryService.findUserContactsByUserId(userId);
		if (userContact != null) {
			return CommonVOUtil.success(userContact, "已绑定");
		}

		return CommonVOUtil.success("未绑定");
	}


}
