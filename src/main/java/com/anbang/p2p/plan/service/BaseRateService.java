package com.anbang.p2p.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.p2p.plan.bean.BaseLoan;
import com.anbang.p2p.plan.bean.BaseRateOfInterest;
import com.anbang.p2p.plan.dao.BaseLoanDao;
import com.anbang.p2p.plan.dao.BaseRateOfInterestDao;

@Service
public class BaseRateService {

	@Autowired
	private BaseLoanDao baseLoanDao;

	@Autowired
	private BaseRateOfInterestDao baseRateOfInterestDao;

	/**
	 * 基础设置
	 */
	public void setBaseLoan(BaseLoan loan) {
		baseLoanDao.save(loan);
	}

	/**
	 * 利率设置
	 */
	public void setBaseRateOfInterest(BaseRateOfInterest rate) {
		baseRateOfInterestDao.save(rate);
	}
}
