package com.anbang.p2p.plan.bean;

public class BaseRateOfInterest {
	private String id;
	private double seven;// 七日利率
	private double fifteen;// 十五日利率
	private double thirty;// 三十日利率
	private double overdue;// 逾期利率

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getSeven() {
		return seven;
	}

	public void setSeven(double seven) {
		this.seven = seven;
	}

	public double getFifteen() {
		return fifteen;
	}

	public void setFifteen(double fifteen) {
		this.fifteen = fifteen;
	}

	public double getThirty() {
		return thirty;
	}

	public void setThirty(double thirty) {
		this.thirty = thirty;
	}

	public double getOverdue() {
		return overdue;
	}

	public void setOverdue(double overdue) {
		this.overdue = overdue;
	}

}
