package com.anbang.p2p.cqrs.q.dbo;

/**
 * 用户信息
 */
public class UserDbo {
	private String id;// 用户id
	private String nickname;// 用户昵称
	private String headimgurl;// 头像
	private String phone;// 手机号码

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
}