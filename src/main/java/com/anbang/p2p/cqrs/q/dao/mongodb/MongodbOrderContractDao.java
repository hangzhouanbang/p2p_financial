package com.anbang.p2p.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.q.dao.OrderContractDao;
import com.anbang.p2p.cqrs.q.dao.mongodb.repository.OrderContractRepository;
import com.anbang.p2p.cqrs.q.dbo.OrderContract;

@Component
public class MongodbOrderContractDao implements OrderContractDao {

	@Autowired
	private OrderContractRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(OrderContract contract) {
		repository.save(contract);
	}

	@Override
	public OrderContract findById(String contractId) {
		return repository.findOne(contractId);
	}

	@Override
	public long getAmount() {
		return repository.count();
	}

	@Override
	public List<OrderContract> findOrderContract(int page, int size) {
		Query query = new Query();
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, OrderContract.class);
	}

}
