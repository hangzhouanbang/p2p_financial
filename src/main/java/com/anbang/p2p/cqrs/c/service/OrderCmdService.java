package com.anbang.p2p.cqrs.c.service;

import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.domain.order.UserHasOrderAlreadyException;

public interface OrderCmdService {

	OrderValueObject createOrder(String userId, String bankCardNo, Double amount, Double service_charge_rate,
			Integer dayNum, Long freeOfInterest, Double overdue_rate, Double rate, Long currentTime)
			throws UserHasOrderAlreadyException;
}
