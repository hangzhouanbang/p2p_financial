package com.anbang.p2p.cqrs.q.dbo;

/**
 * 用户信用信息
 */
public class UserCreditInfo {
	private String id;// 用户id
	private String auth_id;// 查询用户信用状态的id
	private String describe;// 查询结果的描述

	public boolean finishCreditVerify() {
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuth_id() {
		return auth_id;
	}

	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
}
