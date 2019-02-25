package com.anbang.p2p.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.q.dao.UserBaseInfoDao;
import com.anbang.p2p.cqrs.q.dao.mongodb.repository.UserBaseInfoRepository;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;

@Component
public class MongodbUserBaseInfoDao implements UserBaseInfoDao {

	@Autowired
	private UserBaseInfoRepository repository;

	@Override
	public void save(UserBaseInfo info) {
		repository.save(info);
	}

	@Override
	public UserBaseInfo findById(String userId) {
		return repository.findOne(userId);
	}

}
