package com.anbang.p2p.cqrs.c.service;

import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.domain.order.UserHasOrderAlreadyException;

public interface OrderCmdService {

	OrderValueObject createOrder(String userId, String bankCardNo, Double amount, Double service_charge_rate,
			Long freeTimeOfInterest, Long overdue, Double overdue_rate, Double rate, Integer dayNum, String contractId,
			Long currentTime) throws UserHasOrderAlreadyException;

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
}
