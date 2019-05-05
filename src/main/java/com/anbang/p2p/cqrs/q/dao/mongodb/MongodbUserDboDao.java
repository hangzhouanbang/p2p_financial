package com.anbang.p2p.cqrs.q.dao.mongodb;

import java.util.List;

import com.anbang.p2p.web.vo.UserQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
	public long getAmount(UserQuery userQuery) {
		Query query = new Query();
		if (StringUtils.isNotBlank(userQuery.getPhone())) {
			query.addCriteria(Criteria.where("phone").is(userQuery.getPhone()));
		}
		if (StringUtils.isNotBlank(userQuery.getRealName())) {
			query.addCriteria(Criteria.where("realName").regex(userQuery.getRealName()));
		}
		if (userQuery.getVerify() != null) {
			query.addCriteria(Criteria.where("isVerify").is(userQuery.getVerify()));
		}
		if (StringUtils.isNotBlank(userQuery.getState())) {
			query.addCriteria(Criteria.where("state").is(userQuery.getState()));
		}
		return repository.count();
	}

	@Override
	public List<UserDbo> find(int page, int size, UserQuery userQuery) {
		Query query = new Query();
		if (StringUtils.isNotBlank(userQuery.getPhone())) {
			query.addCriteria(Criteria.where("phone").is(userQuery.getPhone()));
		}
		if (StringUtils.isNotBlank(userQuery.getRealName())) {
			query.addCriteria(Criteria.where("realName").regex(userQuery.getRealName()));
		}
		if (userQuery.getVerify() != null) {
			query.addCriteria(Criteria.where("isVerify").is(userQuery.getVerify()));
		}
		if (StringUtils.isNotBlank(userQuery.getState())) {
			query.addCriteria(Criteria.where("state").is(userQuery.getState()));
		}
		query.with(new Sort(Sort.Direction.DESC, "createTime"));
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
	public void updataVerify(String userId, Boolean isVerify, String realName, String IDcard) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(userId));
		Update update = new Update();
		if (isVerify != null) {
			update.set("isVerify", isVerify);
		}

		if (StringUtils.isNotBlank(realName)) {
			update.set("realName", realName);
		}

		if (StringUtils.isNotBlank(IDcard)) {
			update.set("IDcard", IDcard);
		}

		mongoTemplate.updateFirst(query, update, UserDbo.class);
	}

	@Override
	public void updateCountAndState(String userId, Integer orderCount, Integer overdueCount, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(userId));
		Update update = new Update();
		if (orderCount != null) {
			update.set("orderCount", orderCount);
		}

		if (overdueCount != null) {
			update.set("overdueCount", overdueCount);
		}

		if (StringUtils.isNotBlank(state)) {
			update.set("state", state);
		}
		mongoTemplate.updateFirst(query, update, UserDbo.class);
	}

	@Override
	public void updateUserState(String userId, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(userId));
		Update update = new Update();
		update.set("state", state);
		mongoTemplate.updateFirst(query, update, UserDbo.class);
	}
}
