package com.anbang.p2p.cqrs.q.dao;

import java.util.List;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.q.dbo.LoanOrder;
import com.anbang.p2p.web.vo.LoanOrderQueryVO;

public interface LoanOrderDao {

	void save(LoanOrder order);

	long getAmount(LoanOrderQueryVO query);

	List<LoanOrder> find(int page, int size, LoanOrderQueryVO query);

	LoanOrder findByUserIdAndState(String userId, OrderState state);

	LoanOrder findById(String orderId);
}
