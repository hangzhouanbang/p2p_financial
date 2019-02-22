package com.anbang.p2p.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.plan.bean.UserBaseRateOfInterest;
import com.anbang.p2p.plan.dao.UserBaseRateOfInterestDao;
import com.anbang.p2p.plan.dao.mongodb.repository.UserBaseRateOfInterestRepository;

@Component
public class MongodbUserBaseRateOfInterestDao implements UserBaseRateOfInterestDao {

	@Autowired
	private UserBaseRateOfInterestRepository userBaseRateOfInterestRepository;

	@Override
	public void save(UserBaseRateOfInterest rate) {
		userBaseRateOfInterestRepository.save(rate);
	}

	@Override
	public UserBaseRateOfInterest findByUserId(String userId) {
		return userBaseRateOfInterestRepository.findOne(userId);
	}

}
