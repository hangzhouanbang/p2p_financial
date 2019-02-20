package com.anbang.p2p.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.plan.bean.BaseRateOfInterest;
import com.anbang.p2p.plan.dao.BaseRateOfInterestDao;
import com.anbang.p2p.plan.dao.mongodb.repository.BaseRateOfInterestRepository;

@Component
public class MongodbBaseRateOfInterestDao implements BaseRateOfInterestDao {

	@Autowired
	private BaseRateOfInterestRepository baseRateOfInterestRepository;

	@Override
	public void save(BaseRateOfInterest rate) {
		baseRateOfInterestRepository.save(rate);
	}

}
