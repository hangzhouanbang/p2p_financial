package com.anbang.p2p.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.q.dao.UserAgentInfoDao;
import com.anbang.p2p.cqrs.q.dao.mongodb.repository.UserAgentInfoRepository;
import com.anbang.p2p.cqrs.q.dbo.UserAgentInfo;

@Component
public class MongodbUserAgentInfoDao implements UserAgentInfoDao {

	@Autowired
	private UserAgentInfoRepository repository;

	@Override
	public void save(UserAgentInfo info) {
		repository.save(info);
	}

	@Override
	public UserAgentInfo findById(String userId) {
		return repository.findOne(userId);
	}

}
