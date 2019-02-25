package com.anbang.p2p.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.q.dao.UserCreditInfoDao;
import com.anbang.p2p.cqrs.q.dao.mongodb.repository.UserCreditInfoRepository;
import com.anbang.p2p.cqrs.q.dbo.UserCreditInfo;

@Component
public class MongodbUserCreditInfoDao implements UserCreditInfoDao {

	@Autowired
	private UserCreditInfoRepository repository;

	@Override
	public void save(UserCreditInfo info) {
		repository.save(info);
	}

	@Override
	public UserCreditInfo findById(String userId) {
		return repository.findOne(userId);
	}

}
