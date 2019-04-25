package com.anbang.p2p.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.p2p.plan.bean.BaseLoan;
import com.anbang.p2p.plan.bean.UserBaseLoan;
import com.anbang.p2p.plan.dao.UserBaseLoanDao;

@Service
public class BaseRateService {

	@Autowired
	private UserBaseLoanDao userBaseLoanDao;

	public void changeBaseLoan(double baseLimit, double service_charge, double expand_charge, long overdue, long freeTimeOfInterest,
							   double overdue_rate) {
		BaseLoan.change(baseLimit, service_charge, expand_charge, overdue, freeTimeOfInterest, overdue_rate);
	}

//	public void changeBaseRateOfInterest(double seven_rate, double fifteen_rate, double thirty_rate,
//			double overdue_rate) {
//		BaseRateOfInterest.change(seven_rate, fifteen_rate, thirty_rate, overdue_rate);
//	}

	public void saveUserBaseLoan(UserBaseLoan loan) {
		userBaseLoanDao.save(loan);
	}

	public UserBaseLoan findUserBaseLoanByUserId(String userId) {
		return userBaseLoanDao.findByUserId(userId);
	}


}
