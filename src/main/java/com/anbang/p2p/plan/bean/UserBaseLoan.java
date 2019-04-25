package com.anbang.p2p.plan.bean;

/**
 * 用户基本贷款
 */
public class UserBaseLoan {
	private String id;// userId
	private double baseLimit;// 基本额度
	public double service_charge;// 借款手续费
	public double expand_charge ;	//延期手续费
	private long overdue;// 逾期转催收时间 -
	private long freeTimeOfInterest;// 免息时间-
	private double overdue_rate;	//逾期利率-


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

	public double getExpand_charge() {
		return expand_charge;
	}

	public void setExpand_charge(double expand_charge) {
		this.expand_charge = expand_charge;
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

	public double getOverdue_rate() {
		return overdue_rate;
	}

	public void setOverdue_rate(double overdue_rate) {
		this.overdue_rate = overdue_rate;
	}
}
