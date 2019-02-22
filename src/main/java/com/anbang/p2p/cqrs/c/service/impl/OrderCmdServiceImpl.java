package com.anbang.p2p.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.c.domain.order.OrderManager;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.domain.order.UserHasOrderAlreadyException;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;

@Component
public class OrderCmdServiceImpl extends CmdServiceBase implements OrderCmdService {

	@Override
	public OrderValueObject createOrder(String userId, String bankCardNo, Double amount, Double service_charge_rate,
			Integer dayNum, Long freeOfInterest, Double overdue_rate, Double rate, Long currentTime)
			throws UserHasOrderAlreadyException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.createOrder(userId, bankCardNo, amount, service_charge_rate, dayNum, freeOfInterest,
				overdue_rate, rate, currentTime);
	}

}
