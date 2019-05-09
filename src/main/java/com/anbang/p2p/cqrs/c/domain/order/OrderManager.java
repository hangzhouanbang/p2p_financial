package com.anbang.p2p.cqrs.c.domain.order;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.anbang.p2p.constants.ExpandType;
import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;

/**
 * 卡密管理
 */
public class OrderManager {

	private Map<String, Order> userIdOrderMap = new HashMap<>();

	/**
	 * 申请卡密
	 */
	public OrderValueObject createOrder(String userId, String payType, String payAccount, double amount, double service_charge,
			long freeTimeOfInterest, long overdue, double overdue_rate, int dayNum, String contractId, double expand_charge,
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
		order.setPayType(payType);
		order.setPayAccount(payAccount);
		order.setAmount(amount);
		order.setDayNum(dayNum);
		order.setFreeTimeOfInterest(freeTimeOfInterest);
		order.setContractId(contractId);

		order.setService_charge(service_charge);
		order.setExpand_charge(expand_charge);
		order.setOverdue(overdue);

		order.setOverdue_rate(overdue_rate);
		order.setState(OrderState.check_by_fengkong);
		order.setCreateTime(currentTime);

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
	 * 转管理员审核
	 */
	public OrderValueObject changeOrderStateToCheck_by_admin(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		if (!userIdOrderMap.containsKey(userId)) {
			throw new OrderNotFoundException();
		}
		Order order = userIdOrderMap.get(userId);
		if (!order.getState().equals(OrderState.check_by_fengkong) && !order.getState().equals(OrderState.wait)) {
			throw new IllegalOperationException();
		}
		order.setState(OrderState.check_by_admin);
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
				&& !order.getState().equals(OrderState.check_by_admin)) {
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
		if (!order.getState().equals(OrderState.refund) && !order.getState().equals(OrderState.overdue) &&
				!order.getState().equals(OrderState.collection)) {
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
	public OrderValueObject changeOrderStateToOverdue(String userId)
			throws OrderNotFoundException, IllegalOperationException {
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
	public OrderValueObject changeOrderStateToCollection(String userId)
			throws OrderNotFoundException, IllegalOperationException {
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

	/**
	 * 催收核销
	 */
	public OrderValueObject changeOrderStateClean(String userId)
			throws OrderNotFoundException, IllegalOperationException {
		if (!userIdOrderMap.containsKey(userId)) {
			throw new OrderNotFoundException();
		}
		Order order = userIdOrderMap.get(userId);
		if (!order.getState().equals(OrderState.collection)) {
			throw new IllegalOperationException();
		}
		order.setState(OrderState.clean);
		return new OrderValueObject(order);
	}

	/**
	 * 管理员变更状态
	 */
	public OrderValueObject changeOrderStateByAdmin(String id, String userId, OrderState orderState)
			throws OrderNotFoundException {
		if (!userIdOrderMap.containsKey(userId)) {
			throw new OrderNotFoundException();
		}
		Order order = userIdOrderMap.get(userId);

		if (!order.getId().equals(id)) {
			throw new OrderNotFoundException();
		}

		order.setState(orderState);
		if (OrderState.clean.equals(orderState) || OrderState.refuse.equals(orderState)) {
			userIdOrderMap.remove(userId);
		}
		return new OrderValueObject(order);
	}

	/**
	 * 管理员变更延期费
	 */
	public OrderValueObject changeExpandFee(String id, String userId, double fee)
			throws OrderNotFoundException {
		if (!userIdOrderMap.containsKey(userId)) {
			throw new OrderNotFoundException();
		}
		Order order = userIdOrderMap.get(userId);
		if (!order.getId().equals(id)) {
			throw new OrderNotFoundException();
		}

		double newExpandTotal = order.getExpandTotal() + fee;
		order.setExpandTotal(newExpandTotal);
		return new OrderValueObject(order);
	}

	/**
	 * 管理员变更状态
	 */
	public OrderValueObject addExpand(String id, String userId, ExpandType expandType)
			throws OrderNotFoundException {
		if (!userIdOrderMap.containsKey(userId)) {
			throw new OrderNotFoundException();
		}
		Order order = userIdOrderMap.get(userId);
		if (!order.getId().equals(id)) {
			throw new OrderNotFoundException();
		}

		order.addExpand(expandType);
		return new OrderValueObject(order);
	}

}
