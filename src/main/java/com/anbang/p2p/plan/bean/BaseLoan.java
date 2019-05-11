package com.anbang.p2p.plan.bean;

/**
 * 基本贷款
 */
public class BaseLoan {
	public static double baseLimit = 1500;// 基本额度
	public static double service_charge = 500;// 借款手续费
	public static double expand_charge = 500;	//延期手续费
	public static long overdue = 24L * 60 * 60 * 1000 * 5;// 逾期转催收时间
	public static long freeTimeOfInterest = 24L * 60 * 60 * 1000 * 6;// 免息时间
	public static double overdue_rate = 0.09;// 逾期利率

	public static void change(double baseLimit, double service_charge, double expand_charge, long overdue,
							  long freeTimeOfInterest, double overdue_rate) {
		BaseLoan.baseLimit = baseLimit;
		BaseLoan.service_charge = service_charge;
		BaseLoan.expand_charge = expand_charge;
		BaseLoan.overdue = overdue;
		BaseLoan.freeTimeOfInterest = freeTimeOfInterest;
		BaseLoan.overdue_rate = overdue_rate;
	}
}
