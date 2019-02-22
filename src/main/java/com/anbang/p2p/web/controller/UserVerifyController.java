package com.anbang.p2p.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.plan.bean.IDCardQueryInfo;
import com.anbang.p2p.plan.bean.IDCardVerifyInfo;
import com.anbang.p2p.plan.service.BaseVerifyService;
import com.anbang.p2p.util.HmacSha1Sign;
import com.anbang.p2p.util.HttpUtil;
import com.anbang.p2p.web.vo.CommonVO;
import com.google.gson.Gson;

@RestController
@RequestMapping("/verify")
public class UserVerifyController {

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	private BaseVerifyService baseVerifyService;

	@Autowired
	private UserAuthQueryService userAuthQueryService;

	private Gson gson = new Gson();

	/**
	 * 身份认证
	 */
	@RequestMapping("/baseinfo")
	public CommonVO baseInfo(String token) {
		CommonVO vo = new CommonVO();
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		// TODO 验证是否已经认证
		IDCardVerifyInfo verifyInfo = new IDCardVerifyInfo();
		verifyInfo.setUserId(userId);
		verifyInfo.setToken(token);
		String biz_no = UUID.randomUUID().toString().replaceAll("-", "");
		verifyInfo.setBiz_no(biz_no);
		String response = get_biz_token(biz_no);
		if (StringUtil.isBlank(response)) {
			vo.setSuccess(false);
			vo.setMsg("get biz token fail");
			return vo;
		}
		Map map = gson.fromJson(response, Map.class);
		String request_id = "";
		String time_used = "";
		String biz_token = "";
		String error = "";
		if (map.containsKey("request_id")) {
			request_id = (String) map.get("request_id");
		}
		if (map.containsKey("time_used")) {
			time_used = (String) map.get("time_used");
		}
		verifyInfo.setRequest_id(request_id);
		verifyInfo.setTime_used(time_used);
		if (map.containsKey("biz_token")) {
			biz_token = (String) map.get("biz_token");
			verifyInfo.setBiz_token(biz_token);
			String verifyUrl = "https://openapi.faceid.com/lite_ocr/v1/do/" + biz_token;
			Map data = new HashMap<>();
			vo.setData(data);
			data.put("url", verifyUrl);
		} else if (map.containsKey("error")) {
			error = (String) map.get("error");
			verifyInfo.setError(error);
			vo.setSuccess(false);
			vo.setMsg(error);
		}
		baseVerifyService.saveIDCardVerifyInfo(verifyInfo);
		return vo;
	}

	/**
	 * 处理认证notify
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/baseinfo_notify")
	public String toBaseInfoNotify(HttpServletRequest request) {
		// 获取notify过来的json
		String data = request.getParameter("data");
		return String.format("%s", data);
	}

	/**
	 * 查询身份证认证详情
	 */
	@RequestMapping("/baseinfo_query")
	public CommonVO queryBaseInfo(String biz_token) {
		CommonVO vo = new CommonVO();
		IDCardVerifyInfo verifyInfo = baseVerifyService.findIDCardVerifyInfoByBiz_token(biz_token);
		if (verifyInfo == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid biz_token");
			return vo;
		}
		String response = get_result(biz_token);
		if (StringUtil.isBlank(response)) {
			vo.setSuccess(false);
			vo.setMsg("get biz token fail");
			return vo;
		}
		Map map = gson.fromJson(response, Map.class);
		IDCardQueryInfo info = new IDCardQueryInfo(map);
		// TODO 更新用户基本信息
		return vo;
	}

	private String get_biz_token(String biz_no) {
		String result = "";
		String host = "https://openapi.faceid.com";
		String path = "/lite_ocr/v1/get_biz_token";
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("sign_version", "hmac_sha1");// 签名算法版本，当前仅支持：hmac_sha1
		querys.put("capture_image", "0");// 0:双面，1:人像面
		// 用户完成或取消识别后网页跳转的目标URL（回调方法为get
		// return_url?biz_token=xxx），URL限定为http或https且非内网地址
		querys.put("return_url", "");
		// 用户完成验证、取消验证、或验证超时后，由FaceID服务器请求客户服务器的URL，
		// 我们将会把比对结果同时完整发送至您的服务器，用于对客户端的结果进行验证（回调方法为Post）， URL限定为http或https且非内网地址
		querys.put("notify_url", "");
		// "默认为空"。客户业务流水号，建议设置为您的业务相关的流水串号并且唯一。此字段不超过128字节
		querys.put("biz_no", biz_no);
		// 限定真是身份证阀值（推荐值0.8），如存在某一面低于设定值则重新拍摄
		querys.put("idcard_threshold", "0.8");
		// 0:身份证必须完整，1:身份证内容区域必须都在图片内，2:不做限定，如某一面不满足要求则重新拍摄
		querys.put("limit_completeness", "0");
		// 限定图片质量，取［0，1］区间实数，3位有效数字，一般来说质量分低于0.753则认为存在质量问题，如存在某一面低于设定值则重新拍摄
		querys.put("limit_quality", "0.800");
		// 0:进行逻辑判断，1:不进行逻辑判断，如存在某一面低于设定值则重新拍摄
		querys.put("limit_logic", "1");
		try {
			String sign = HmacSha1Sign.genSign("apiKey", "secretKey", 0L);
			querys.put("sign", sign);// 根据API_KEY和API_SECRET生成的签名
			HttpResponse response = HttpUtil.doPost(host, path, headers, querys, "");
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String get_result(String biz_token) {
		String result = "";
		String host = "https://openapi.faceid.com";
		String path = "/lite_ocr/v1/get_result";
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("sign_version", "hmac_sha1");// 签名算法版本，当前仅支持：hmac_sha1
		querys.put("biz_token", biz_token);// get_biz_token接口返回的biz_token
		querys.put("need_image", "1");// 是否需要返回身份证正反面照片及头像照片，1:返回、0:不返回
		try {
			String sign = HmacSha1Sign.genSign("apiKey", "secretKey", 0L);
			querys.put("sign", sign);// 根据API_KEY和API_SECRET生成的签名
			HttpResponse response = HttpUtil.doGet(host, path, headers, querys);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
