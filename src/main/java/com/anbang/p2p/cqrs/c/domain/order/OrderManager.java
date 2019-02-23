package com.anbang.p2p.cqrs.c.domain.order;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;

/**
 * 卡密管理
 */
public class OrderManager {

	private Map<String, Order> userIdOrderMap = new HashMap<>();

	/**
	 * 申请卡密
	 */
	public OrderValueObject createOrder(String userId, String bankCardNo, double amount, double service_charge_rate,
			long freeTimeOfInterest, long overdue, double overdue_rate, double rate, int dayNum, String contractId,
			long currentTime) throws UserHasOrderAlreadyException {
		if (userIdOrderMap.containsKey(userId)) {
			throw new UserHasOrderAlreadyException();
		}
		// 生成卡密
		Random r = new Random(currentTime);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String orderId = format.format(currentTime) + r.nextInt(10) + userId + r.nextInt(10);
		Order order = new Order();
		order.setId(orderId);
		order.setUserId(userId);
		order.setBankCardNo(bankCardNo);
		order.setAmount(amount);
		order.setDayNum(dayNum);
		order.setService_charge_rate(service_charge_rate);
		order.setFreeTimeOfInterest(freeTimeOfInterest);
		order.setContractId(contractId);
		order.setRate(rate);
		order.setOverdue_rate(overdue_rate);
		order.setState(OrderState.check_by_fengkong);
		order.setCreateTime(currentTime);
		// 计算手续费
		order.calculateServiceCharge();
		// 计算实际到账金额
		order.calculateRealAmount();
		// 计算最大还款日期
		order.calculateMaxLimitTime();
		userIdOrderMap.put(userId, order);
		return new OrderValueObject(order);
	}

	/**
	 * 拒绝放款
	 */
	public OrderValueObject refuseOrder(String userId) throws OrderNotFoundException, IllegalOperationException {
		if (!userIdOrderMap.containsKey(userId)) {
			throw new OrderNotFoundException();
		}
		Order order = userIdOrderMap.get(userId);
		if (!order.getState().equals(OrderState.check_by_admin)) {
			throw new IllegalOperationException();
		}
		order.setState(OrderState.refuse);
		userIdOrderMap.remove(userId);
		return new OrderValueObject(order);
	}

	/**
	 * 等待放款
	 */
	public OrderValueObject changeOrderStateToWait(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		if (!userIdOrderMap.containsKey(userId)) {
			throw new OrderNotFoundException();
		}
		Order order = userIdOrderMap.get(userId);
		if (!order.getState().equals(OrderState.check_by_fengkong)
				|| !order.getState().equals(OrderState.check_by_admin)) {
			throw new IllegalOperationException();
		}
		order.setState(OrderState.wait);
		return new OrderValueObject(order);
	}

	/**
	 * 放款
	 */
	public OrderValueObject changeOrderStateToRefund(String userId, long currentTime)
			throws OrderNotFoundException, IllegalOperationException {
		if (!userIdOrderMap.containsKey(userId)) {
			throw new OrderNotFoundException();
		}
		Order order = userIdOrderMap.get(userId);
		if (!order.getState().equals(OrderState.wait)) {
			throw new IllegalOperationException();
		}
		order.setDeliverTime(currentTime);
		order.setState(OrderState.refund);
		return new OrderValueObject(order);
	}

	/**
	 * 结算
	 */
	public OrderValueObject cleanOrder(String userId, double amount, long currentTime)
			throws OrderNotFoundException, IllegalOperationException {
		if (!userIdOrderMap.containsKey(userId)) {
			throw new OrderNotFoundException();
		}
		Order order = userIdOrderMap.get(userId);
		if (!order.getState().equals(OrderState.refund)) {
			throw new IllegalOperationException();
		}
		order.setRealRefundAmount(amount);
		order.setRefundTime(currentTime);
		order.setState(OrderState.clean);
		userIdOrderMap.remove(userId);
		return new OrderValueObject(order);
	}

	/**
	 * 逾期
	 */
	public OrderValueObject overdueOrder(String userId) throws OrderNotFoundException, IllegalOperationException {
		if (!userIdOrderMap.containsKey(userId)) {
			throw new OrderNotFoundException();
		}
		Order order = userIdOrderMap.get(userId);
		if (!order.getState().equals(OrderState.refund)) {
			throw new IllegalOperationException();
		}
		order.setState(OrderState.overdue);
		return new OrderValueObject(order);
	}

	/**
	 * 催收
	 */
	public OrderValueObject collectOrder(String userId) throws OrderNotFoundException, IllegalOperationException {
		if (!userIdOrderMap.containsKey(userId)) {
			throw new OrderNotFoundException();
		}
		Order order = userIdOrderMap.get(userId);
		if (!order.getState().equals(OrderState.overdue)) {
			throw new IllegalOperationException();
		}
		order.setState(OrderState.collection);
		return new OrderValueObject(order);
	}

}
