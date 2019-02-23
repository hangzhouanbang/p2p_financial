package com.anbang.p2p.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.p2p.plan.bean.BaseLoan;
import com.anbang.p2p.plan.bean.BaseRateOfInterest;
import com.anbang.p2p.plan.bean.UserBaseLoan;
import com.anbang.p2p.plan.bean.UserBaseRateOfInterest;
import com.anbang.p2p.plan.dao.UserBaseLoanDao;
import com.anbang.p2p.plan.dao.UserBaseRateOfInterestDao;

@Service
public class BaseRateService {

	@Autowired
	private UserBaseLoanDao userBaseLoanDao;

	@Autowired
	private UserBaseRateOfInterestDao userBaseRateOfInterestDao;

	public void changeBaseLoan(double baseLimit, double service_charge, long overdue, long freeTimeOfInterest) {
		BaseLoan.change(baseLimit, service_charge, overdue, freeTimeOfInterest);
	}

	public void changeBaseRateOfInterest(double seven_rate, double fifteen_rate, double thirty_rate,
			double overdue_rate) {
		BaseRateOfInterest.change(seven_rate, fifteen_rate, thirty_rate, overdue_rate);
	}

	public void saveUserBaseLoan(UserBaseLoan loan) {
		userBaseLoanDao.save(loan);
	}

	public UserBaseLoan findUserBaseLoanByUserId(String userId) {
		return userBaseLoanDao.findByUserId(userId);
	}

	public void saveUserBaseRateOfInterest(UserBaseRateOfInterest rate) {
		userBaseRateOfInterestDao.save(rate);
	}

	public UserBaseRateOfInterest findUserBaseRateOfInterestByUserId(String userId) {
		return userBaseRateOfInterestDao.findByUserId(userId);
	}
}
