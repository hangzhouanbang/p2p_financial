package com.anbang.p2p.plan.bean;

/**
 * 身份证验证信息
 *
 */
public class IDCardVerifyInfo {
	private String id;
	private String userId;// 用户id
	private String token;// 验证时用的token
	private String biz_no;// biz_no,业务流水号
	private String request_id;// request_id,用于标示本次请求的唯一的字符串
	private String time_used;// 整个请求所花费的时间
	private String biztoken;// 可用biz_token拼接对应身份证OCR页面URL
	private String error;// 错误原因

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBiz_no() {
		return biz_no;
	}

	public void setBiz_no(String biz_no) {
		this.biz_no = biz_no;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public String getTime_used() {
		return time_used;
	}

	public void setTime_used(String time_used) {
		this.time_used = time_used;
	}

	public String getBiztoken() {
		return biztoken;
	}

	public void setBiztoken(String biztoken) {
		this.biztoken = biztoken;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
