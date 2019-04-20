package com.anbang.p2p.web.vo;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;

public class LoanOrderQueryVO {
	private String userId;
	private String phone;
	private String realName;
	private OrderState state;

	private Long nowTime;	//用于和应还时间比较maxLimitTime

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

	public Long getNowTime() {
		return nowTime;
	}

	public void setNowTime(Long nowTime) {
		this.nowTime = nowTime;
	}
}
