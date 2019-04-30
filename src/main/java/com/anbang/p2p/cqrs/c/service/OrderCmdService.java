package com.anbang.p2p.cqrs.c.service;

import com.anbang.p2p.constants.ExpandType;
import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.domain.order.UserHasOrderAlreadyException;

public interface OrderCmdService {

	OrderValueObject createOrder(String userId, String payType, String payAccount, Double amount, Double service_charge,
			Long freeTimeOfInterest, Long overdue, Double overdue_rate, Integer dayNum, String contractId,
			Double expand_charge, Long currentTime) throws UserHasOrderAlreadyException;

	OrderValueObject refuseOrder(String userId) throws OrderNotFoundException, IllegalOperationException;

	OrderValueObject changeOrderStateToWait(String userId) throws OrderNotFoundException, IllegalOperationException;

	OrderValueObject changeOrderStateToCheck_by_admin(String userId)
			throws OrderNotFoundException, IllegalOperationException;

	OrderValueObject changeOrderStateToRefund(String userId, Long currentTime)
			throws OrderNotFoundException, IllegalOperationException;

	OrderValueObject cleanOrder(String userId, Double amount, Long currentTime)
			throws OrderNotFoundException, IllegalOperationException;

	OrderValueObject changeOrderStateToOverdue(String userId) throws OrderNotFoundException, IllegalOperationException;

	OrderValueObject changeOrderStateToCollection(String userId)
			throws OrderNotFoundException, IllegalOperationException;

	OrderValueObject changeOrderStateClean(String userId)
			throws OrderNotFoundException, IllegalOperationException;

	OrderValueObject changeOrderStateByAdmin(String id, String userId, OrderState orderState)
			throws OrderNotFoundException;

	OrderValueObject changeExpandFee(String id, String userId, Double fee)
			throws OrderNotFoundException;

	OrderValueObject addExpand(String id, String userId, ExpandType expandType)
			throws OrderNotFoundException;
}
