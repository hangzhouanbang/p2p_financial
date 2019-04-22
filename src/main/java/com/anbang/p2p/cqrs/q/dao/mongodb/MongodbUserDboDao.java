package com.anbang.p2p.cqrs.q.dao.mongodb;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, UserDbo.class);
	}

	@Override
	public void updateNicknameAndHeadimgurlById(String userId, String nickname, String headimgurl) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(userId));
		Update update = new Update();
		update.set("nickname", nickname);
		update.set("headimgurl", headimgurl);
		mongoTemplate.updateFirst(query, update, UserDbo.class);
	}

	@Override
	public void updateIPById(String userId, String loginIp, String ipAddress) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(userId));
		Update update = new Update();
		update.set("loginIp", loginIp);
		update.set("ipAddress", ipAddress);
		mongoTemplate.updateFirst(query, update, UserDbo.class);
	}

	@Override
	public void updataVerify(String userId, Boolean isVerify, String realName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(userId));
		Update update = new Update();
		if (isVerify != null) {
			update.set("isVerify", isVerify);
		}

		if (StringUtils.isNotBlank(realName)) {
			update.set("realName", realName);
		}
		mongoTemplate.updateFirst(query, update, UserDbo.class);
	}

	@Override
	public void updateCount(String userId, Integer orderCount, Integer overdueCount) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(userId));
		Update update = new Update();
		if (orderCount != null) {
			update.set("orderCount", orderCount);
		}

		if (overdueCount != null) {
			update.set("overdueCount", overdueCount);
		}
		mongoTemplate.updateFirst(query, update, UserDbo.class);
	}

}
