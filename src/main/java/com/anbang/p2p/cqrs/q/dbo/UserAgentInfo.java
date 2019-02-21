package com.anbang.p2p.cqrs.q.dbo;

/**
 * 用户运营商信息
 */
public class UserAgentInfo {
	private String id;// 用户id
	private String agent;// 运营商

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

}
