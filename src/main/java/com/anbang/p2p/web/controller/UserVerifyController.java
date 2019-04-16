package com.anbang.p2p.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.anbang.p2p.conf.VerifyConfig;
import com.anbang.p2p.constants.VerifyRecordState;
import com.anbang.p2p.plan.bean.VerifyRecord;
import com.anbang.p2p.plan.service.RiskService;
import com.anbang.p2p.plan.service.VerifyRecordService;
import com.anbang.p2p.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserContacts;
import com.anbang.p2p.cqrs.q.dbo.UserCreditInfo;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.plan.bean.IDCardQueryInfo;
import com.anbang.p2p.plan.bean.IDCardVerifyInfo;
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

	@Autowired
	private RiskService riskService;

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
		record.setState(VerifyRecordState.INIT);
		record.setCauseBy("加密加签");
		record.setCreateTime(System.currentTimeMillis());
		verifyRecordService.save(record);

		String partner_order_id = record.getId();
		String pub_key = VerifyConfig.PUB_KEY;
		String sign_time = TimeUtils.getStringDate(new Date());
		String sign = RiskUtil.getMD5Sign(record.getId(), sign_time);
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
	 * 云慧眼身份认证回调
	 */
	@RequestMapping("/verifyCallback")
	public void verifyCallback(String partner_order_id, String result_auth, String result_status, String errorcode, String message) {
		if (StringUtils.isBlank(partner_order_id)) {
			return;
		}

		VerifyRecord record = verifyRecordService.getById(partner_order_id);
		if (record != null) {
			// 人脸认证成功
			if ("T".equals(result_auth)) {
				verifyRecordService.updateStateAndCause(record.getId(), VerifyRecordState.SUCCESS, result_auth, message);

				// 查询并保存
				try {
					riskService.orderQuery(partner_order_id, record.getUerId());
				} catch (Exception e) {
					e.printStackTrace();
				}
            } else {
				verifyRecordService.updateStateAndCause(record.getId(), VerifyRecordState.ERROR, result_auth, message);
			}
		}
	}

	@RequestMapping("/getImg")
	public void getImg(String imgName, HttpServletResponse response) {
		ImgSaveUtil.getImg(imgName, response);
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

		userContacts.setId(null);
		userContacts.setUserId(userId);
		userAuthQueryService.saveContacts(userContacts);
		return CommonVOUtil.success("success");
	}

//	/**
//	 * 查询身份证认证详情
//	 */
//	@RequestMapping("/baseinfo_query")
//	public CommonVO queryBaseInfo(String biz_token) {
//		CommonVO vo = new CommonVO();
//		IDCardVerifyInfo verifyInfo = baseVerifyService.findIDCardVerifyInfoByBiz_token(biz_token);
//		if (verifyInfo == null) {
//			vo.setSuccess(false);
//			vo.setMsg("invalid biz_token");
//			return vo;
//		}
//		String response = getBaseInfo_result(biz_token);
//		if (StringUtil.isBlank(response)) {
//			vo.setSuccess(false);
//			vo.setMsg("get biz token fail");
//			return vo;
//		}
//		Map map = gson.fromJson(response, Map.class);
//		IDCardQueryInfo info = new IDCardQueryInfo(map);
//		// TODO 更新用户基本信息
//		return vo;
//	}
//
//	private String getBaseInfo_biz_token(String biz_no) {
//		String result = "";
//		String host = "https://openapi.faceid.com";
//		String path = "/lite_ocr/v1/get_biz_token";
//		Map<String, String> headers = new HashMap<String, String>();
//		Map<String, String> querys = new HashMap<String, String>();
//		querys.put("sign_version", "hmac_sha1");// 签名算法版本，当前仅支持：hmac_sha1
//		querys.put("capture_image", "0");// 0:双面，1:人像面
//		// 用户完成或取消识别后网页跳转的目标URL（回调方法为get
//		// return_url?biz_token=xxx），URL限定为http或https且非内网地址
//		querys.put("return_url", "");
//		// 用户完成验证、取消验证、或验证超时后，由FaceID服务器请求客户服务器的URL，
//		// 我们将会把比对结果同时完整发送至您的服务器，用于对客户端的结果进行验证（回调方法为Post）， URL限定为http或https且非内网地址
//		querys.put("notify_url", "");
//		// "默认为空"。客户业务流水号，建议设置为您的业务相关的流水串号并且唯一。此字段不超过128字节
//		querys.put("biz_no", biz_no);
//		// 限定真是身份证阀值（推荐值0.8），如存在某一面低于设定值则重新拍摄
//		querys.put("idcard_threshold", "0.8");
//		// 0:身份证必须完整，1:身份证内容区域必须都在图片内，2:不做限定，如某一面不满足要求则重新拍摄
//		querys.put("limit_completeness", "0");
//		// 限定图片质量，取［0，1］区间实数，3位有效数字，一般来说质量分低于0.753则认为存在质量问题，如存在某一面低于设定值则重新拍摄
//		querys.put("limit_quality", "0.800");
//		// 0:进行逻辑判断，1:不进行逻辑判断，如存在某一面低于设定值则重新拍摄
//		querys.put("limit_logic", "1");
//		try {
//			String sign = HmacSha1Sign.genSign("apiKey", "secretKey", 0L);
//			querys.put("sign", sign);// 根据API_KEY和API_SECRET生成的签名
//			HttpResponse response = HttpUtil.doPost(host, path, headers, querys, "");
//			result = EntityUtils.toString(response.getEntity());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	private String getBaseInfo_result(String biz_token) {
//		String result = "";
//		String host = "https://openapi.faceid.com";
//		String path = "/lite_ocr/v1/get_result";
//		Map<String, String> headers = new HashMap<String, String>();
//		Map<String, String> querys = new HashMap<String, String>();
//		querys.put("sign_version", "hmac_sha1");// 签名算法版本，当前仅支持：hmac_sha1
//		querys.put("biz_token", biz_token);// get_biz_token接口返回的biz_token
//		querys.put("need_image", "1");// 是否需要返回身份证正反面照片及头像照片，1:返回、0:不返回
//		try {
//			String sign = HmacSha1Sign.genSign("apiKey", "secretKey", 0L);
//			querys.put("sign", sign);// 根据API_KEY和API_SECRET生成的签名
//			HttpResponse response = HttpUtil.doGet(host, path, headers, querys);
//			result = EntityUtils.toString(response.getEntity());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
}
