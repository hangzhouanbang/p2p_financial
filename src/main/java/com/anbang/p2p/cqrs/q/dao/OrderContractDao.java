package com.anbang.p2p.cqrs.q.dao;

import java.util.List;

import com.anbang.p2p.cqrs.q.dbo.OrderContract;

public interface OrderContractDao {

	void save(OrderContract contract);

	OrderContract findById(String contractId);

	long getAmount();

	List<OrderContract> findOrderContract(int page, int size);
}
