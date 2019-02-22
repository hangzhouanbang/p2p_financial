package com.anbang.p2p.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.plan.bean.UserBaseLoan;
import com.anbang.p2p.plan.dao.UserBaseLoanDao;
import com.anbang.p2p.plan.dao.mongodb.repository.UserBaseLoanRepository;

@Component
public class MongodbUserBaseLoanDao implements UserBaseLoanDao {

	@Autowired
	private UserBaseLoanRepository userBaseLoanRepository;

	@Override
	public void save(UserBaseLoan loan) {
		userBaseLoanRepository.save(loan);
	}

	@Override
	public UserBaseLoan findByUserId(String userId) {
		return userBaseLoanRepository.findOne(userId);
	}

}
