package com.anbang.p2p.web.vo;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;

public class LoanOrderQueryVO {
	private String userId;
	private String phone;
	private String realName;
	private OrderState state;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

}
