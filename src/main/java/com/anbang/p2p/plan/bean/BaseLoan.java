package com.anbang.p2p.plan.bean;

public class BaseLoan {
	private String id;
	private double baseLimit;// 基本额度
	private double service_charge;// 手续费比例
	private long overdue;// 逾期时间
	private long freeOfInterest;// 免息天数

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getBaseLimit() {
		return baseLimit;
	}

	public void setBaseLimit(double baseLimit) {
		this.baseLimit = baseLimit;
	}

	public double getService_charge() {
		return service_charge;
	}

	public void setService_charge(double service_charge) {
		this.service_charge = service_charge;
	}

	public long getOverdue() {
		return overdue;
	}

	public void setOverdue(long overdue) {
		this.overdue = overdue;
	}

	public long getFreeOfInterest() {
		return freeOfInterest;
	}

	public void setFreeOfInterest(long freeOfInterest) {
		this.freeOfInterest = freeOfInterest;
	}

}
