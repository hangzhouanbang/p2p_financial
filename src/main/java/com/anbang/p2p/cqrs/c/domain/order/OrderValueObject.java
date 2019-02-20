package com.anbang.p2p.cqrs.c.domain.order;

public class OrderValueObject {
	private String id;// 卡密
	private String userId;// 用户
	private String bankCardId;// 银行卡号
	private double amount;// 贷款金额
	private double service_charge;// 手续费
	private int freeOfInterest;// 免息天数
	private long maxLimitTime;// 最大还款日期
	private double rate;// 每日利率
	private double overdue;// 逾期利率
	private OrderState state;// 卡密状态
	private long createTime;// 创建时间
	private long refundTime;// 实际还款日期
	private double realRefundAmount;// 实际还款
	private double realAmount;// 实际到账
	private long deliverTime;// 放款时间

	public OrderValueObject(Order order) {
		this.id = order.getId();
		this.userId = order.getUserId();
		this.bankCardId = order.getBankCardId();
		this.amount = order.getAmount();
		this.service_charge = order.getService_charge();
		this.freeOfInterest = order.getFreeOfInterest();
		this.maxLimitTime = order.getMaxLimitTime();
		this.rate = order.getRate();
		this.overdue = order.getOverdue();
		this.state = order.getState();
		this.createTime = order.getCreateTime();
		this.refundTime = order.getRefundTime();
		this.realRefundAmount = order.getRealRefundAmount();
		this.realAmount = order.getRealAmount();
		this.deliverTime = order.getDeliverTime();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(String bankCardId) {
		this.bankCardId = bankCardId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getService_charge() {
		return service_charge;
	}

	public void setService_charge(double service_charge) {
		this.service_charge = service_charge;
	}

	public int getFreeOfInterest() {
		return freeOfInterest;
	}

	public void setFreeOfInterest(int freeOfInterest) {
		this.freeOfInterest = freeOfInterest;
	}

	public long getMaxLimitTime() {
		return maxLimitTime;
	}

	public void setMaxLimitTime(long maxLimitTime) {
		this.maxLimitTime = maxLimitTime;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getOverdue() {
		return overdue;
	}

	public void setOverdue(double overdue) {
		this.overdue = overdue;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(long refundTime) {
		this.refundTime = refundTime;
	}

	public double getRealRefundAmount() {
		return realRefundAmount;
	}

	public void setRealRefundAmount(double realRefundAmount) {
		this.realRefundAmount = realRefundAmount;
	}

	public double getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(double realAmount) {
		this.realAmount = realAmount;
	}

	public long getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(long deliverTime) {
		this.deliverTime = deliverTime;
	}

}
