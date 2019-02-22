package com.anbang.p2p.plan.bean;

import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;

/**
 * 基本利率
 */
public class BaseRateOfInterest {
	public static double seven_rate = 0.001;// 七日利率
	public static double fifteen_rate = 0.0012;// 十五日利率
	public static double thirty_rate = 0.0015;// 三十日利率
	public static double overdue_rate = 0.002;// 逾期利率

	public static void change(double seven_rate, double fifteen_rate, double thirty_rate, double overdue_rate) {
		BaseRateOfInterest.seven_rate = seven_rate;
		BaseRateOfInterest.fifteen_rate = fifteen_rate;
		BaseRateOfInterest.thirty_rate = thirty_rate;
		BaseRateOfInterest.overdue_rate = overdue_rate;
	}

	/**
	 * 获取利率
	 */
	public static double getRateByDayNum(int dayNum) throws IllegalOperationException {
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
}
