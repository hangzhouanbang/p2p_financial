package com.anbang.p2p.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
			Integer dayNum, Long freeOfInterest, Double overdue_rate, Double rate, Long currentTime)
			throws UserHasOrderAlreadyException {
		CommonCommand cmd = new CommonCommand(OrderCmdServiceImpl.class.getName(), "createOrder", userId, bankCardNo,
				amount, service_charge_rate, dayNum, freeOfInterest, overdue_rate, rate, currentTime);

		DeferredResult<OrderValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			OrderValueObject orderValueObject = orderCmdServiceImpl.createOrder(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter());
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

}
