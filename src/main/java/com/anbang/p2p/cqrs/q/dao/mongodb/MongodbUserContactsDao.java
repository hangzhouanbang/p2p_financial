package com.anbang.p2p.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.q.dao.UserContactsDao;
import com.anbang.p2p.cqrs.q.dao.mongodb.repository.UserContactsRepository;
import com.anbang.p2p.cqrs.q.dbo.UserContacts;

@Component
public class MongodbUserContactsDao implements UserContactsDao {

	@Autowired
	private UserContactsRepository repository;

	@Override
	public void save(UserContacts contacts) {
		repository.save(contacts);
	}

	@Override
	public UserContacts findById(String userId) {
		return repository.findOne(userId);
	}

}
