package com.anbang.p2p.web.controller;

import java.util.Date;
import java.util.regex.Pattern;

import com.anbang.p2p.conf.VerifyConfig;
import com.anbang.p2p.constants.CommonRecordState;
import com.anbang.p2p.plan.bean.MobileVerify;
import com.anbang.p2p.plan.bean.ShoppingVerify;
import com.anbang.p2p.plan.bean.VerifyRecord;
import com.anbang.p2p.plan.service.VerifyRecordService;
import com.anbang.p2p.util.*;
import com.anbang.p2p.util.checkservice.MobileServiceUtil;
import com.anbang.p2p.util.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserContacts;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.web.vo.CommonVO;

@CrossOrigin
@RestController
@RequestMapping("/verify")
public class UserVerifyController {

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	private UserAuthQueryService userAuthQueryService;

	@Autowired
	private VerifyRecordService verifyRecordService;

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

		return CommonVOUtil.error("未绑定");
	}

	/**
	 * 获取运营商认证url
	 */
	@RequestMapping("/getMobileUrl")
	public CommonVO getMobileUrl (String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.error("invalid token");
		}

		String time = TimeUtils.getStringDate(new Date());
		String taskId = userId + "_" + time;
		String url = XinyanUtil.getCarrierUrl(taskId);

		return CommonVOUtil.success(url,"等待中");
	}

	/**
	 * 获取taobao认证url
	 */
	@RequestMapping("/getShoppingUrl")
	public CommonVO getShoppingUrl (String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.error("invalid token");
		}

		String time = TimeUtils.getStringDate(new Date());
		String taskId = userId + "_" + time;
		String url = XinyanUtil.getTaobaowebUrl(taskId);

		return CommonVOUtil.success(url,"等待中");
	}

	/**
	 * 运营商认证情况查询
	 */
	@RequestMapping("/checkMobile")
	public CommonVO checkMobile (String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.error("invalid token");
		}

		MobileVerify mobileVerify = userAuthQueryService.getMobileVerify(userId);
		if (mobileVerify == null) {
			return CommonVOUtil.error("未绑定");
		}

		if (CommonRecordState.SUCCESS.equals(mobileVerify.getState())) {
			return CommonVOUtil.success(mobileVerify, "success");
		}

		return CommonVOUtil.error("等待中");
	}

	/**
	 * 电商认证情况查询
	 */
	@RequestMapping("/checkShopping")
	public CommonVO checkShopping (String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.error("invalid token");
		}

		ShoppingVerify shoppingVerify = userAuthQueryService.getShoppingVerify(userId);
		if (shoppingVerify == null) {
			return CommonVOUtil.error("未绑定");
		}

		if (CommonRecordState.SUCCESS.equals(shoppingVerify.getState())) {
			return CommonVOUtil.success(shoppingVerify, "success");
		}

		return CommonVOUtil.error("等待中");
	}

//	/**
//	 * 运营商验证,获取验证码
//	 */
//	@RequestMapping("/mobileVerify")
//	public CommonVO mobileVerify (String token, String username, String password) {
//		String userId = userAuthService.getUserIdBySessionId(token);
//		if (userId == null) {
//			return CommonVOUtil.error("invalid token");
//		}
//
//		UserBaseInfo baseInfo = userAuthQueryService.findUserBaseInfoByUserId(userId);
//		if (baseInfo == null) {
//			return CommonVOUtil.error("请先进行身份认证");
//		}
//
//		MobileVerify mobileVerify = new MobileVerify();
//		mobileVerify.setId(userId);
//		mobileVerify.setIDcard(baseInfo.getIDcard());
//		mobileVerify.setRealName(baseInfo.getRealName());
//		mobileVerify.setUsername(username);
//		mobileVerify.setPassword(password);
//		mobileVerify.setState(CommonRecordState.INIT);
//		mobileVerify.setCreateTime(System.currentTimeMillis());
//
//		try {
//			String key =  mobileServiceUtil.startTask(userId, baseInfo.getIDcard(), baseInfo.getRealName(), username, password);
//			mobileVerify.setToken(key);
//			userAuthQueryService.saveMobileVerify(mobileVerify);
//			return CommonVOUtil.success(key,"success");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return CommonVOUtil.error("运营商认证失败");
//		}
//	}
//
//	/**
//	 * 运营商验证码输入
//	 */
//	@RequestMapping("/mobileCode")
//	public CommonVO mobileCode (String token, String input) {
//		String userId = userAuthService.getUserIdBySessionId(token);
//		if (userId == null) {
//			return CommonVOUtil.error("invalid token");
//		}
//
//		if (StringUtils.isBlank(input)) {
//			return CommonVOUtil.invalidParam();
//		}
//
//		MobileVerify mobileVerify = userAuthQueryService.getMobileVerify(userId);
//		if (mobileVerify == null) {
//			return CommonVOUtil.error("认证异常");
//		}
//
//		try {
//			String query_token = mobileServiceUtil.input(mobileVerify.getToken(), input);
//			return CommonVOUtil.success(query_token,"success");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return CommonVOUtil.error("运营商认证失败");
//		}
//	}

}
