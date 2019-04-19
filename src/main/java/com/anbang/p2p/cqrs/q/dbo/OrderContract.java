package com.anbang.p2p.cqrs.q.dbo;

/**
 * 订单合同
 */
public class OrderContract {
	private String id;// 合同号
	private String body;
	private String desc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
