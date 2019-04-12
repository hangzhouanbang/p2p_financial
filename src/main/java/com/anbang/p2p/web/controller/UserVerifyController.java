package com.anbang.p2p.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.anbang.p2p.conf.VerifyConfig;
import com.anbang.p2p.constants.VerifyRecordState;
import com.anbang.p2p.plan.bean.VerifyRecord;
import com.anbang.p2p.plan.service.VerifyRecordService;
import com.anbang.p2p.util.CommonVoUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.dbo.UserAgentInfo;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserContacts;
import com.anbang.p2p.cqrs.q.dbo.UserCreditInfo;
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

	@Autowired
	private VerifyRecordService verifyRecordService;

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
		UserBaseInfo baseInfo = userAuthQueryService.findUserBaseInfoByUserId(userId);
		if (baseInfo != null && baseInfo.finishUserVerify()) {
			vo.setSuccess(false);
			vo.setMsg("finish verify");
			return vo;
		}
		baseInfo = new UserBaseInfo();
		baseInfo.setId(userId);
		baseInfo.setIDcard("33019566695612323");
		baseInfo.setIDcardImgUrl_front(
				"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551161768209&di=109b309590b4d54e40bf6417a697410c&imgtype=0&src=http%3A%2F%2Fimg1.gamersky.com%2Fimage2012%2F03%2F20120313s_5%2F10.jpg");
		baseInfo.setIDcardImgUrl_reverse(
				"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3163144905,873716841&fm=26&gp=0.jpg");
		baseInfo.setRealName("利鲁姆");
		baseInfo.setFaceImgUrl(
				"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551161725755&di=7ffe5ff12e81d2b764a723c07676f3ea&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn09%2F216%2Fw640h376%2F20181124%2F167c-hmivixn8459064.jpg");
		userAuthQueryService.saveUserBaseInfo(baseInfo);
		// IDCardVerifyInfo verifyInfo = new IDCardVerifyInfo();
		// verifyInfo.setUserId(userId);
		// verifyInfo.setToken(token);
		// String biz_no = UUID.randomUUID().toString().replaceAll("-", "");
		// verifyInfo.setBiz_no(biz_no);
		// String response = getBaseInfo_biz_token(biz_no);
		// if (StringUtil.isBlank(response)) {
		// vo.setSuccess(false);
		// vo.setMsg("get biz token fail");
		// return vo;
		// }
		// Map map = gson.fromJson(response, Map.class);
		// String request_id = "";
		// String time_used = "";
		// String biz_token = "";
		// String error = "";
		// if (map.containsKey("request_id")) {
		// request_id = (String) map.get("request_id");
		// }
		// if (map.containsKey("time_used")) {
		// time_used = (String) map.get("time_used");
		// }
		// verifyInfo.setRequest_id(request_id);
		// verifyInfo.setTime_used(time_used);
		// if (map.containsKey("biz_token")) {
		// biz_token = (String) map.get("biz_token");
		// verifyInfo.setBiztoken(biz_token);
		// String verifyUrl = "https://openapi.faceid.com/lite_ocr/v1/do/" + biz_token;
		// Map data = new HashMap<>();
		// vo.setData(data);
		// data.put("url", verifyUrl);
		// } else if (map.containsKey("error")) {
		// error = (String) map.get("error");
		// verifyInfo.setError(error);
		// vo.setSuccess(false);
		// vo.setMsg(error);
		// }
		// baseVerifyService.saveIDCardVerifyInfo(verifyInfo);
		return vo;
	}

	/**
	 * 云慧眼加密加签
	 */
	@RequestMapping("/agentinfo")
	public CommonVO agentInfo(String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVoUtil.error("invalid token");
		}

		VerifyRecord record = new VerifyRecord();
		record.setUerId(userId);
		record.setState(VerifyRecordState.INIT);
		record.setCauseBy("加密加签");
		record.setCreateTime(System.currentTimeMillis());
		verifyRecordService.save(record);

		String partner_order_id = record.getId();
		String pub_key = VerifyConfig.PUB_KEY;
		String sign = "";
		String sign_time = "";




		return CommonVoUtil.success("success");
	}

	/**
	 * 紧急联系人
	 */
	@RequestMapping("/contacts")
	public CommonVO contacts(String token, UserContacts userContacts) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVoUtil.error("invalid token");
		}
		if (userContacts == null || UserContacts.isBlank(userContacts)) {
			return CommonVoUtil.error("invalid param");
		}
		if (!Pattern.matches("[0-9]{11}", userContacts.getCommonContactsPhone()) ||
				!Pattern.matches("[0-9]{11}", userContacts.getDirectContactsPhone())) {// 检验手机格式
			return CommonVoUtil.error("invalid phone");
		}

		userContacts.setId(null);
		userContacts.setUserId(userId);
		userAuthQueryService.saveContacts(userContacts);
		return CommonVoUtil.success("success");
	}

	/**
	 * 芝麻认证
	 */
	@RequestMapping("/creditinfo")
	public CommonVO creditInfo(String token) {
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
			vo.setMsg("finish verify");
			return vo;
		}
		creditInfo = new UserCreditInfo();
		creditInfo.setId(userId);
		creditInfo.setAuth_id("f5d4g56df");
		creditInfo.setDescribe("信用良好");
		userAuthQueryService.saveUserCreditInfo(creditInfo);
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
		String response = getBaseInfo_result(biz_token);
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

	private String getBaseInfo_biz_token(String biz_no) {
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

	private String getBaseInfo_result(String biz_token) {
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
