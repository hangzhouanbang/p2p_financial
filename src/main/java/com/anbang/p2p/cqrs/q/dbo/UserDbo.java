package com.anbang.p2p.cqrs.q.dbo;

/**
 * 用户信息
 */
public class UserDbo {
	private String id;// 用户id
	private String nickname;// 用户昵称
	private String headimgurl;// 头像
	private String phone;// 手机号码
	private AlipayInfo alipayInfo;	//支付宝账号信息
	private boolean isVerify;	//实名
	private String realName;	//真实姓名
	private long createTime;	//注册日期

	private String loginIp;
	private String ipAddress;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public AlipayInfo getAlipayInfo() {
		return alipayInfo;
	}

	public void setAlipayInfo(AlipayInfo alipayInfo) {
		this.alipayInfo = alipayInfo;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
