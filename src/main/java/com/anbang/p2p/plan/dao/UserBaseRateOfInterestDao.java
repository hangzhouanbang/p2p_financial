package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.UserBaseRateOfInterest;

public interface UserBaseRateOfInterestDao {

	void save(UserBaseRateOfInterest rate);

	UserBaseRateOfInterest findByUserId(String userId);
}
