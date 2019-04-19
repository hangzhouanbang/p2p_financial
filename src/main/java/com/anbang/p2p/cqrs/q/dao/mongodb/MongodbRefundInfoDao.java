package com.anbang.p2p.cqrs.q.dao.mongodb;

import java.util.List;

import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.q.dao.RefundInfoDao;
import com.anbang.p2p.cqrs.q.dao.mongodb.repository.RefundInfoRepository;
import com.anbang.p2p.cqrs.q.dbo.RefundInfo;

@Component
public class MongodbRefundInfoDao implements RefundInfoDao {

	@Autowired
	private RefundInfoRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(RefundInfo info) {
		repository.save(info);
	}

	@Override
	public long getAmountByUserId(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		return mongoTemplate.count(query, RefundInfo.class);
	}

	@Override
	public RefundInfo getById(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, RefundInfo.class);
	}

	@Override
	public List<RefundInfo> findByUserId(int page, int size, String userId) {
		Query query = new Query();
		if (StringUtil.isBlank(userId)) {
			query.addCriteria(Criteria.where("userId").is(userId));
		}
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, RefundInfo.class);
	}

	@Override
	public void updateStatus(String id, String status) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("status", status);
		mongoTemplate.updateFirst(query, update, RefundInfo.class);
	}

}
