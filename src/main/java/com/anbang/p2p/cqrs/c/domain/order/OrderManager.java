package com.anbang.p2p.cqrs.c.domain.order;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class OrderManager {

	private Map<String, Order> userIdOrderMap = new HashMap<>();

	public OrderValueObject createOrder(String userId, String bankCardNo, double amount, double service_charge_rate,
			int dayNum, long freeOfInterest, double overdue_rate, double rate, long currentTime)
			throws UserHasOrderAlreadyException {
		if (userIdOrderMap.containsKey(userId)) {
			throw new UserHasOrderAlreadyException();
		}
		Order order = new Order();
		// 生成卡密
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String orderId = format.format(currentTime) + userId + (int) (Math.random() * (999 - 100 + 1) + 100);
		order.setId(orderId);
		order.setUserId(userId);
		order.setBankCardNo(bankCardNo);
		order.setAmount(amount);
		order.setRate(rate);
		order.setOverdue_rate(overdue_rate);
		order.setState(OrderState.check);
		order.setCreateTime(currentTime);
		order.setFreeOfInterest(freeOfInterest);
		// 计算手续费
		order.calculateServiceCharge(service_charge_rate);
		// 计算最大还款日期
		order.calculateMaxLimitTime(dayNum);
		userIdOrderMap.put(userId, order);
		return new OrderValueObject(order);
	}
}
