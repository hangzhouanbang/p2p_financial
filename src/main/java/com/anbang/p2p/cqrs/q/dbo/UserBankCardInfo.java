package com.anbang.p2p.cqrs.q.dbo;

/**
 * 用户银行卡信息
 */
public class UserBankCardInfo {
	private String id;
	private String userId;// 用户id
	private String userName;
	private String bankCardNo;// 银行卡号
	private String phone;	// 银行卡预留手机号

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

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
