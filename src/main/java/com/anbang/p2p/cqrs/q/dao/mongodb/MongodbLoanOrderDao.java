package com.anbang.p2p.cqrs.q.dao.mongodb;

import java.util.List;

import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.q.dao.LoanOrderDao;
import com.anbang.p2p.cqrs.q.dao.mongodb.repository.LoanOrderRepository;
import com.anbang.p2p.cqrs.q.dbo.LoanOrder;
import com.anbang.p2p.web.vo.LoanOrderQueryVO;

@Component
public class MongodbLoanOrderDao implements LoanOrderDao {

	@Autowired
	private LoanOrderRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(LoanOrder order) {
		repository.save(order);
	}

	@Override
	public long getAmount(LoanOrderQueryVO queryVO) {
		Query query = new Query();
		if (!StringUtil.isBlank(queryVO.getUserId())) {
			query.addCriteria(Criteria.where("userId").is(queryVO.getUserId()));
		}
		if (!StringUtil.isBlank(queryVO.getPhone())) {
			query.addCriteria(Criteria.where("phone").regex(queryVO.getPhone()));
		}
		if (!StringUtil.isBlank(queryVO.getRealName())) {
			query.addCriteria(Criteria.where("realName").regex(queryVO.getRealName()));
		}
		if (queryVO.getState() != null) {
			query.addCriteria(Criteria.where("state").is(queryVO.getState()));
		}
		return mongoTemplate.count(query, LoanOrder.class);
	}

	@Override
	public List<LoanOrder> find(int page, int size, LoanOrderQueryVO queryVO) {
		Query query = new Query();
		if (!StringUtil.isBlank(queryVO.getUserId())) {
			query.addCriteria(Criteria.where("userId").is(queryVO.getUserId()));
		}
		if (!StringUtil.isBlank(queryVO.getPhone())) {
			query.addCriteria(Criteria.where("phone").regex(queryVO.getPhone()));
		}
		if (!StringUtil.isBlank(queryVO.getRealName())) {
			query.addCriteria(Criteria.where("realName").regex(queryVO.getRealName()));
		}
		if (queryVO.getState() != null) {
			query.addCriteria(Criteria.where("state").is(queryVO.getState()));
		}
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, LoanOrder.class);
	}

	@Override
	public LoanOrder findByUserIdAndState(String userId, OrderState state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		query.addCriteria(Criteria.where("state").is(state));
		return mongoTemplate.findOne(query, LoanOrder.class);
	}

	@Override
	public LoanOrder findById(String orderId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(orderId));
		return mongoTemplate.findOne(query, LoanOrder.class);
	}

}
