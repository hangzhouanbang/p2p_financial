package com.anbang.p2p.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.q.dao.UserBankCardInfoDao;
import com.anbang.p2p.cqrs.q.dao.mongodb.repository.UserBankCardInfoRepository;
import com.anbang.p2p.cqrs.q.dbo.UserBankCardInfo;

@Component
public class MongodbUserBankCardInfoDao implements UserBankCardInfoDao {

	@Autowired
	private UserBankCardInfoRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(UserBankCardInfo info) {
		repository.save(info);
	}

	@Override
	public long getAmountByUserId(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		return mongoTemplate.count(query, UserBankCardInfo.class);
	}

	@Override
	public List<UserBankCardInfo> findByUserId(int page, int size, String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		return mongoTemplate.find(query, UserBankCardInfo.class);
	}

	@Override
	public UserBankCardInfo findById(String cardId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(cardId));
		return mongoTemplate.findOne(query, UserBankCardInfo.class);
	}

}
