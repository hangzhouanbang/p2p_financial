package com.anbang.p2p.web.controller;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.anbang.p2p.util.*;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.conf.AliyunConfig;
import com.anbang.p2p.cqrs.c.domain.user.CreateUserResult;
import com.anbang.p2p.cqrs.c.service.UserAuthCmdService;
import com.anbang.p2p.cqrs.c.service.UserAuthService;
import com.anbang.p2p.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.p2p.cqrs.q.service.UserAuthQueryService;
import com.anbang.p2p.plan.bean.UserVerifyPhoneInfo;
import com.anbang.p2p.plan.service.PhoneVerifyService;
import com.anbang.p2p.web.vo.CommonVO;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 授权验证
 * 
 * @author lsc
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/auth")
public class UserAuthController {

	@Autowired
	private PhoneVerifyService phoneVerifyService;

	@Autowired
	private UserAuthCmdService userAuthCmdService;

	@Autowired
	private UserAuthQueryService userAuthQueryService;

	@Autowired
	private UserAuthService userAuthService;

	private Gson gson = new Gson();

	/**
	 * 获取验证码
	 */
	@RequestMapping("/check")
	@ResponseBody
	public CommonVO check(String token) {
		String userId = userAuthService.getUserIdBySessionId(token);
		if (userId == null) {
			return CommonVOUtil.invalidToken();
		}
		return CommonVOUtil.success(token, "success");
	}

	/**
	 * 获取验证码
	 */
	@RequestMapping("/getcode")
	@ResponseBody
	public CommonVO getVerifyCode(String phone) {
		CommonVO vo = new CommonVO();
		if (!Pattern.matches("[0-9]{11}", phone)) {// 检验手机格式
			vo.setSuccess(false);
			vo.setMsg("invalid phone");
			return vo;
		}
		UserVerifyPhoneInfo info = phoneVerifyService.findUserVerifyPhoneInfoByPhone(phone);
		if (info != null && System.currentTimeMillis() - info.getCreateTime() < 60 * 1000) {// 重新获取验证码需要间隔一分钟
			vo.setSuccess(false);
			vo.setMsg("too frequently");
			return vo;
		}
		String param = VerifyPhoneCodeUtil.generateVerifyCode();
		String response = getCode(phone, param);
		Map map = gson.fromJson(response, Map.class);
		String message = "";
		String code = "";
		String requestId = "";
		String bizId = "";
		if (map.containsKey("Message")) {
			message = (String) map.get("Message");
		}
		if (map.containsKey("Code")) {
			code = (String) map.get("Code");
		}
		if (message.equals("OK") && code.equals("OK")) {
			if (map.containsKey("RequestId")) {
				requestId = (String) map.get("RequestId");
			}
			if (map.containsKey("BizId")) {
				bizId = (String) map.get("BizId");
			}
		} else {
			vo.setSuccess(false);
			vo.setMsg(message);
		}
		UserVerifyPhoneInfo userVerifyPhoneInfo = new UserVerifyPhoneInfo();
		userVerifyPhoneInfo.setId(phone);
		userVerifyPhoneInfo.setParam(param);
		userVerifyPhoneInfo.setMessage(message);
		userVerifyPhoneInfo.setCode(code);
		userVerifyPhoneInfo.setRequestId(requestId);
		userVerifyPhoneInfo.setBizId(bizId);
		userVerifyPhoneInfo.setInvalidTime(System.currentTimeMillis() + 20 * 60 * 1000);// 有效时间二十分钟
		userVerifyPhoneInfo.setCreateTime(System.currentTimeMillis());
		phoneVerifyService.saveVerifyPhoneCode(userVerifyPhoneInfo);
		return CommonVOUtil.success("success");
	}

	/**
	 * 验证手机验证码
	 */
	@RequestMapping("/verify")
	@ResponseBody
	public CommonVO verifyPhone(String phone, String code, HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		UserVerifyPhoneInfo verifyInfo = phoneVerifyService.findUserVerifyPhoneInfoByPhone(phone);
		if (verifyInfo == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid phone");
			return vo;
		}
		if (verifyInfo.isUsed() || !verifyInfo.getParam().equals(code)
				|| System.currentTimeMillis() > verifyInfo.getInvalidTime()) {// 验证码错误或者已失效
			vo.setSuccess(false);
			vo.setMsg("invalid code");
			return vo;
		}
		verifyInfo.setUsed(true);
		phoneVerifyService.saveVerifyPhoneCode(verifyInfo);

		String loginIp = IPUtil.getRealIp(request);
		String ipAddress = IPAddressUtil.getIPAddress(loginIp);

		try {
			AuthorizationDbo authDbo = userAuthQueryService.findAuthorizationDbo("p2p.app.phone", phone);
			if (authDbo != null) {// 已经手机注册
				userAuthQueryService.updateIPById(authDbo.getUserId(), loginIp, ipAddress); //更新ip

				String token = userAuthService.thirdAuth("p2p.app.phone", phone);
				Map data = new HashMap<>();
				vo.setData(data);
				data.put("token", token);
				return vo;
			} else {
				// 新用户手机注册
				CreateUserResult createResult = userAuthCmdService.createUserAndAddThirdAuth("p2p.app.phone", phone,
						System.currentTimeMillis());
				userAuthQueryService.createUserAndAddThirdAuth(createResult.getUserId(), createResult.getPublisher(),
						createResult.getUuid(), loginIp, ipAddress);
				String token = userAuthService.thirdAuth(createResult.getPublisher(), createResult.getUuid());
				Map data = new HashMap<>();
				vo.setData(data);
				data.put("token", token);
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
	}

	private String getCode(String phone, String param) {
		String host = "https://feginesms.market.alicloudapi.com";
		String path = "/codeNotice";
		String appcode = AliyunConfig.APPCODE;
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();

		querys.put("param", param);
		querys.put("phone", phone);
		querys.put("sign", "1");
		querys.put("skin", "1");
		String result = "";
		try {
			HttpResponse response = HttpUtil.doGet(host, path, headers, querys);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 验证手机验证码
	 */
	@RequestMapping("/test")
	@ResponseBody
	public CommonVO verifyPhone(String phone) {
		CommonVO vo = new CommonVO();

		try {
			AuthorizationDbo authDbo = userAuthQueryService.findAuthorizationDbo("p2p.app.phone", phone);
			if (authDbo != null) {// 已经手机注册
				String token = userAuthService.thirdAuth("p2p.app.phone", phone);
				Map data = new HashMap<>();
				vo.setData(data);
				data.put("token", token);
				return vo;
			} else {
				// 新用户手机注册
				CreateUserResult createResult = userAuthCmdService.createUserAndAddThirdAuth("p2p.app.phone", phone,
						System.currentTimeMillis());
				userAuthQueryService.createUserAndAddThirdAuth(createResult.getUserId(), createResult.getPublisher(),
						createResult.getUuid(), "","");
				String token = userAuthService.thirdAuth(createResult.getPublisher(), createResult.getUuid());
				Map data = new HashMap<>();
				vo.setData(data);
				data.put("token", token);
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
	}


	/**
	 * app 下载
	 */
	@RequestMapping("/app")
	@ResponseBody
	public String app(int type, HttpServletResponse response) {
		String filePath = "/data";//被下载的文件在服务器中的路径
		String filename;	//被下载文件的名称
		if (type == 1) {
			filename="kuailu.apk";
		} else {
			filename="kuailu.ipa";
		}

		File file = new File(filePath + "/" + filename);
		if(file.exists()) { //判断文件父目录是否存在
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

			byte[] buffer = new byte[1024];
			FileInputStream fis = null; //文件输入流
			BufferedInputStream bis = null;

			OutputStream os = null; //输出流
			try {
				os = response.getOutputStream();
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer);
					i = bis.read(buffer);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("----------file download" + filename);
			try {
				bis.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return "success";
	}
}
