package com.anbang.p2p.cqrs.q.dbo;

/**
 * 订单合同
 */
public class OrderContract {
	private String id;// 合同号
	private String bankCardNo;// 银行卡号

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

}
