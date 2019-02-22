package com.anbang.p2p.plan.bean;

import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;

/**
 * 用户基本利率
 */
public class UserBaseRateOfInterest {
	private String id;// userId
	private double seven_rate;// 七日利率
	private double fifteen_rate;// 十五日利率
	private double thirty_rate;// 三十日利率
	private double overdue_rate;// 逾期利率

	/**
	 * 获取利率
	 */
	public double getRateByDayNum(int dayNum) throws IllegalOperationException {
		switch (dayNum) {
		case 7:
			return seven_rate;
		case 15:
			return fifteen_rate;
		case 30:
			return thirty_rate;
		default:
			throw new IllegalOperationException();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getSeven_rate() {
		return seven_rate;
	}

	public void setSeven_rate(double seven_rate) {
		this.seven_rate = seven_rate;
	}

	public double getFifteen_rate() {
		return fifteen_rate;
	}

	public void setFifteen_rate(double fifteen_rate) {
		this.fifteen_rate = fifteen_rate;
	}

	public double getThirty_rate() {
		return thirty_rate;
	}

	public void setThirty_rate(double thirty_rate) {
		this.thirty_rate = thirty_rate;
	}

	public double getOverdue_rate() {
		return overdue_rate;
	}

	public void setOverdue_rate(double overdue_rate) {
		this.overdue_rate = overdue_rate;
	}

}
