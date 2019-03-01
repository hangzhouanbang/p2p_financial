package com.anbang.p2p.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.dbo.UserAgentInfo;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserContacts;
import com.anbang.p2p.cqrs.q.dbo.UserCreditInfo;
import com.anbang.p2p.cqrs.q.dbo.UserDbo;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.web.vo.CommonVO;

@RestController
@RequestMapping("/userinfo")
public class UserInfoController {

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	private UserAuthQueryService userAuthQueryService;

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
			vo.setMsg("not finish verify");
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
			vo.setMsg("not finish verify");
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
			vo.setMsg("not finish verify");
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
			vo.setMsg("not finish verify");
			return vo;
		}
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("creditInfo", creditInfo);
		return vo;
	}
}
