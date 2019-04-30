package com.anbang.p2p.cqrs.c.service.disruptor;

import com.anbang.p2p.constants.ExpandType;
import com.anbang.p2p.cqrs.c.domain.order.OrderState;
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

@Component(value = "orderCmdService")
public class DisruptorOrderCmdService extends DisruptorCmdServiceBase implements OrderCmdService {

	@Autowired
	private OrderCmdServiceImpl orderCmdServiceImpl;

	@Override
	public OrderValueObject createOrder(String userId, String payType, String payAccount, Double amount, Double service_charge,
			Long freeTimeOfInterest, Long overdue, Double overdue_rate, Integer dayNum, String contractId,
			Double expand_charge, Long currentTime) throws UserHasOrderAlreadyException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "createOrder", userId, payType, payAccount,
				amount, service_charge, freeTimeOfInterest, overdue, overdue_rate, dayNum, contractId, expand_charge,
				currentTime);

		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.createOrder(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
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

	@Override
	public OrderValueObject changeOrderStateToCheck_by_admin(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "changeOrderStateToCheck_by_admin",
				userId);
		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl
					.changeOrderStateToCheck_by_admin(cmd.getParameter());
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
	public OrderValueObject changeOrderStateClean(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "changeOrderStateClean",
				userId);
		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.changeOrderStateClean(cmd.getParameter());
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
	public OrderValueObject changeOrderStateByAdmin(String id, String userId, OrderState orderState)
			throws OrderNotFoundException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "changeOrderStateByAdmin",
				id, userId, orderState);
		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.changeOrderStateByAdmin(cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return orderValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderNotFoundException) {
				throw (OrderNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}
	@Override
	public OrderValueObject changeExpandFee(String id, String userId, Double fee)
			throws OrderNotFoundException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "changeExpandFee",
				id, userId, fee);
		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.changeExpandFee(cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return orderValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderNotFoundException) {
				throw (OrderNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}
	@Override
	public OrderValueObject addExpand(String id, String userId, ExpandType expandType)
			throws OrderNotFoundException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "addExpand",
				id, userId, expandType);
		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.addExpand(cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return orderValueObject;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderNotFoundException) {
				throw (OrderNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}


}
