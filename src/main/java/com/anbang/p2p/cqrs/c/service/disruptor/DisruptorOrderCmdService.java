package com.anbang.p2p.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;
import com.anbang.p2p.cqrs.c.domain.order.OrderNotFoundException;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;
import com.anbang.p2p.cqrs.c.domain.order.UserHasOrderAlreadyException;
import com.anbang.p2p.cqrs.c.service.OrderCmdService;
import com.anbang.p2p.cqrs.c.service.impl.OrderCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "OrderCmdService")
public class DisruptorOrderCmdService extends DisruptorCmdServiceBase implements OrderCmdService {

	@Autowired
	private OrderCmdServiceImpl orderCmdServiceImpl;

	@Override
	public OrderValueObject createOrder(String userId, String bankCardNo, Double amount, Double service_charge_rate,
			Long freeTimeOfInterest, Long overdue, Double overdue_rate, Double rate, Integer dayNum, String contractId,
			Long currentTime) throws UserHasOrderAlreadyException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "createOrder", userId, bankCardNo,
				amount, service_charge_rate, freeTimeOfInterest, overdue, overdue_rate, rate, dayNum, contractId,
				currentTime);

		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.createOrder(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return orderValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof UserHasOrderAlreadyException) {
				throw (UserHasOrderAlreadyException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public OrderValueObject refuseOrder(String userId) throws OrderNotFoundException, IllegalOperationException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "refuseOrder", userId);
		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.refuseOrder(cmd.getParameter());
			return orderValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderNotFoundException) {
				throw (OrderNotFoundException) e;
			} else if (e instanceof IllegalOperationException) {
				throw (IllegalOperationException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public OrderValueObject changeOrderStateToWait(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "changeOrderStateToWait", userId);
		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.changeOrderStateToWait(cmd.getParameter());
			return orderValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderNotFoundException) {
				throw (OrderNotFoundException) e;
			} else if (e instanceof IllegalOperationException) {
				throw (IllegalOperationException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public OrderValueObject changeOrderStateToRefund(String userId, Long currentTime)
			throws OrderNotFoundException, IllegalOperationException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "changeOrderStateToRefund", userId,
				currentTime);
		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.changeOrderStateToRefund(cmd.getParameter(),
					cmd.getParameter());
			return orderValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderNotFoundException) {
				throw (OrderNotFoundException) e;
			} else if (e instanceof IllegalOperationException) {
				throw (IllegalOperationException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public OrderValueObject cleanOrder(String userId, Double amount, Long currentTime)
			throws OrderNotFoundException, IllegalOperationException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "cleanOrder", userId, amount,
				currentTime);
		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.cleanOrder(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter());
			return orderValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderNotFoundException) {
				throw (OrderNotFoundException) e;
			} else if (e instanceof IllegalOperationException) {
				throw (IllegalOperationException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public OrderValueObject changeOrderStateToOverdue(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "changeOrderStateToOverdue", userId);
		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.changeOrderStateToOverdue(cmd.getParameter());
			return orderValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderNotFoundException) {
				throw (OrderNotFoundException) e;
			} else if (e instanceof IllegalOperationException) {
				throw (IllegalOperationException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public OrderValueObject changeOrderStateToCollection(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "changeOrderStateToCollection",
				userId);
		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.changeOrderStateToCollection(cmd.getParameter());
			return orderValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderNotFoundException) {
				throw (OrderNotFoundException) e;
			} else if (e instanceof IllegalOperationException) {
				throw (IllegalOperationException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

}
