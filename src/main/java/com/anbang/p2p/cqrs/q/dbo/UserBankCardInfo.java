package com.anbang.p2p.cqrs.q.dbo;

/**
 * 用户银行卡信息
 */
public class UserBankCardInfo {
	private String id;
	private String userId;// 用户id
	private String bank;// 银行名称
	private String bankCardId;// 银行卡号

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

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(String bankCardId) {
		this.bankCardId = bankCardId;
	}

}
