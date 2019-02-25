package com.anbang.p2p.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.q.dao.UserDboDao;
import com.anbang.p2p.cqrs.q.dao.mongodb.repository.UserDboRepository;
import com.anbang.p2p.cqrs.q.dbo.UserDbo;

@Component
public class MongodbUserDboDao implements UserDboDao {

	@Autowired
	private UserDboRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(UserDbo dbo) {
		repository.save(dbo);
	}

	@Override
	public UserDbo findById(String userId) {
		return repository.findOne(userId);
	}

	@Override
	public UserDbo findByPhone(String phone) {
		Query query = new Query();
		query.addCriteria(Criteria.where("phone").is(phone));
		return mongoTemplate.findOne(query, UserDbo.class);
	}

	@Override
	public long getAmount() {
		return repository.count();
	}

	@Override
	public List<UserDbo> find(int page, int size) {
		Query query = new Query();
		query.skip((page-1)*size);
		query.limit(size);
		return mongoTemplate.find(query, UserDbo.class);
	}

}
