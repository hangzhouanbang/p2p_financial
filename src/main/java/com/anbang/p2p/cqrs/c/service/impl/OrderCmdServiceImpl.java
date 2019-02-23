package com.anbang.p2p.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.domain.order.OrderManager;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.domain.order.UserHasOrderAlreadyException;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;

@Component
public class OrderCmdServiceImpl extends CmdServiceBase implements OrderCmdService {

	@Override
	public OrderValueObject createOrder(String userId, String bankCardNo, Double amount, Double service_charge_rate,
			Long freeTimeOfInterest, Long overdue, Double overdue_rate, Double rate, Integer dayNum, String contractId,
			Long currentTime) throws UserHasOrderAlreadyException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.createOrder(userId, bankCardNo, amount, service_charge_rate, freeTimeOfInterest, overdue,
				overdue_rate, rate, dayNum, contractId, currentTime);
	}

	@Override
	public OrderValueObject refuseOrder(String userId) throws OrderNotFoundException, IllegalOperationException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.refuseOrder(userId);
	}

	@Override
	public OrderValueObject changeOrderStateToWait(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.changeOrderStateToWait(userId);
	}

	@Override
	public OrderValueObject changeOrderStateToRefund(String userId, Long currentTime)
			throws OrderNotFoundException, IllegalOperationException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.changeOrderStateToRefund(userId, currentTime);
	}

	@Override
	public OrderValueObject cleanOrder(String userId, Double amount, Long currentTime)
			throws OrderNotFoundException, IllegalOperationException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.cleanOrder(userId, amount, currentTime);
	}

	@Override
	public OrderValueObject overdueOrder(String userId) throws OrderNotFoundException, IllegalOperationException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.overdueOrder(userId);
	}

	@Override
	public OrderValueObject collectOrder(String userId) throws OrderNotFoundException, IllegalOperationException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.collectOrder(userId);
	}

}
