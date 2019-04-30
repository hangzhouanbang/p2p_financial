package com.anbang.p2p.cqrs.q.dao.mongodb;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
		if (queryVO.getNowTime() != null) {
			query.addCriteria(Criteria.where("maxLimitTime").lt(queryVO.getNowTime()));
		}

		if (StringUtils.isNotBlank(queryVO.getId())) {
			query.addCriteria(Criteria.where("id").is(queryVO.getId()));
		}
		if (queryVO.getExport() != null) {
			query.addCriteria(Criteria.where("export").is(queryVO.getExport()));
		}
//		if (queryVO.getStartTime() != null || queryVO.getEndTime() != null) {
//			Criteria criteria = Criteria.where("overdueTime");
//			if (queryVO.getStartTime() != null) {
//				criteria = criteria.gte(queryVO.getStartTime());
//			}
//			if (queryVO.getEndTime() != null) {
//				criteria = criteria.lte(queryVO.getEndTime());
//			}
//			query.addCriteria(criteria);
//		}
		if (queryVO.getStartDay() != null || queryVO.getEndDay() != null) {
			Criteria criteria = Criteria.where("overdueDay");
			if (queryVO.getStartDay() != null) {
				criteria = criteria.gte(queryVO.getStartDay());
			}
			if (queryVO.getEndDay() != null) {
				criteria = criteria.lte(queryVO.getEndDay());
			}
			query.addCriteria(criteria);
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
		if (queryVO.getNowTime() != null) {
			query.addCriteria(Criteria.where("maxLimitTime").lt(queryVO.getNowTime()));
		}

		if (StringUtils.isNotBlank(queryVO.getId())) {
			query.addCriteria(Criteria.where("id").is(queryVO.getId()));
		}
		if (queryVO.getExport() != null) {
			query.addCriteria(Criteria.where("export").is(queryVO.getExport()));
		}
//		if (queryVO.getStartTime() != null || queryVO.getEndTime() != null) {
//			Criteria criteria = Criteria.where("overdueTime");
//			if (queryVO.getStartTime() != null) {
//				criteria = criteria.gte(queryVO.getStartTime());
//			}
//			if (queryVO.getEndTime() != null) {
//				criteria = criteria.lte(queryVO.getEndTime());
//			}
//			query.addCriteria(criteria);
//		}
		if (queryVO.getStartDay() != null || queryVO.getEndDay() != null) {
			Criteria criteria = Criteria.where("overdueDay");
			if (queryVO.getStartDay() != null) {
				criteria = criteria.gte(queryVO.getStartDay());
			}
			if (queryVO.getEndDay() != null) {
				criteria = criteria.lte(queryVO.getEndDay());
			}
			query.addCriteria(criteria);
		}

		query.with(queryVO.getSort());
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

	@Override
	public LoanOrder findLastOrderByUserId(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		query.with(new Sort(Sort.Direction.DESC, "createTime"));
		return mongoTemplate.findOne(query, LoanOrder.class);
	}

	@Override
	public void updateExportState(String id, boolean export) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("export", export);
		mongoTemplate.updateFirst(query, update, LoanOrder.class);
	}


	@Override
	public List<LoanOrder> listByIds(String[] ids) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(ids));
		return mongoTemplate.find(query, LoanOrder.class);
	}

	@Override
	public void updateLoanOrderAmount(String id, int overdueDay, double interest, double shouldRepayAmount) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("overdueDay", overdueDay);
		update.set("interest", interest);
		update.set("shouldRepayAmount", shouldRepayAmount);
		mongoTemplate.updateFirst(query, update, LoanOrder.class);
	}

	public void updateLoanOrderState(String id, OrderState orderState, Double amount) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("state", orderState);
		update.set("shouldRepayAmount", amount);
		mongoTemplate.updateFirst(query, update, LoanOrder.class);
	}
}
