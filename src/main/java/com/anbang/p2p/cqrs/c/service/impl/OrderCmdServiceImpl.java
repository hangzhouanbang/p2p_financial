package com.anbang.p2p.cqrs.c.service.impl;

import com.anbang.p2p.constants.ExpandType;
import com.anbang.p2p.cqrs.c.domain.order.*;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;

@Component
public class OrderCmdServiceImpl extends CmdServiceBase implements OrderCmdService {

	@Override
	public OrderValueObject createOrder(String userId, String payType, String payAccount, Double amount, Double service_charge,
			Long freeTimeOfInterest, Long overdue, Double overdue_rate, Integer dayNum, String contractId, Double expand_charge,
			Long currentTime) throws UserHasOrderAlreadyException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.createOrder(userId, payType, payAccount, amount, service_charge, freeTimeOfInterest, overdue,
				overdue_rate, dayNum, contractId, expand_charge, currentTime);
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
	public OrderValueObject changeOrderStateToOverdue(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.changeOrderStateToOverdue(userId);
	}

	@Override
	public OrderValueObject changeOrderStateToCollection(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.changeOrderStateToCollection(userId);
	}

	@Override
	public OrderValueObject changeOrderStateToCheck_by_admin(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.changeOrderStateToCheck_by_admin(userId);
	}

	@Override
	public OrderValueObject changeOrderStateClean(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.changeOrderStateClean(userId);
	}

	@Override
	public OrderValueObject changeOrderStateByAdmin(String id, String userId, OrderState orderState)
			throws OrderNotFoundException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.changeOrderStateByAdmin(id, userId, orderState);
	}

	@Override
	public OrderValueObject changeExpandFee(String id, String userId, Double fee)
			throws OrderNotFoundException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.changeExpandFee(id, userId, fee);
	}

	@Override
	public OrderValueObject addExpand(String id, String userId, ExpandType expandType)
			throws OrderNotFoundException {
		OrderManager orderManager = singletonEntityRepository.getEntity(OrderManager.class);
		return orderManager.addExpand(id, userId, expandType);
	}

}
