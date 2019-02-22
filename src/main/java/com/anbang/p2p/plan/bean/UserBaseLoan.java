package com.anbang.p2p.plan.bean;

/**
 * 用户基本贷款
 */
public class UserBaseLoan {
	private String id;// userId
	private double baseLimit;// 基本额度
	private double service_charge_rate;// 手续费比例
	private long overdue;// 逾期转催收时间
	private long freeTimeOfInterest;// 免息时间

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

	public double getService_charge_rate() {
		return service_charge_rate;
	}

	public void setService_charge_rate(double service_charge_rate) {
		this.service_charge_rate = service_charge_rate;
	}

	public long getOverdue() {
		return overdue;
	}

	public void setOverdue(long overdue) {
		this.overdue = overdue;
	}

	public long getFreeTimeOfInterest() {
		return freeTimeOfInterest;
	}

	public void setFreeTimeOfInterest(long freeTimeOfInterest) {
		this.freeTimeOfInterest = freeTimeOfInterest;
	}

}
