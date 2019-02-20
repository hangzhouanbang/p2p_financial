package com.anbang.p2p.cqrs.c.domain.order;

import java.util.HashMap;
import java.util.Map;

public class OrderManager {

	private Map<String, Order> userIdOrderMap = new HashMap<>();

	public OrderValueObject createOrder() {
		Order order = new Order();
		return new OrderValueObject(order);
	}
}
