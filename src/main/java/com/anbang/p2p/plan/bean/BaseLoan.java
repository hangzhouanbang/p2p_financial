package com.anbang.p2p.plan.bean;

/**
 * 基本贷款
 */
public class BaseLoan {
	public static double baseLimit = 0.1;// 基本额度
	public static double service_charge_rate = 0.02;// 手续费比例
	public static long overdue = 24L * 60 * 60 * 1000 * 7;// 逾期转催收时间
	public static long freeTimeOfInterest = 24L * 60 * 60 * 1000 * 3;// 免息时间

	public static void change(double baseLimit, double service_charge_rate, long overdue, long freeTimeOfInterest) {
		BaseLoan.baseLimit = baseLimit;
		BaseLoan.service_charge_rate = service_charge_rate;
		BaseLoan.overdue = overdue;
		BaseLoan.freeTimeOfInterest = freeTimeOfInterest;
	}
}
