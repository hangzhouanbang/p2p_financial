package com.anbang.p2p.cqrs.c.domain.order;

public class OrderValueObject {
	private String id;// 卡密
	private String userId;// 用户
	private String bankCardNo;// 银行卡号
	private double amount;// 贷款金额
	private int dayNum;// 贷款天数
	private double service_charge_rate;// 手续费比例
	private long freeTimeOfInterest;// 免息时间
	private long overdue;// 逾期转催收时间
	private double rate;// 每日利率
	private double overdue_rate;// 逾期利率
	private OrderState state;// 卡密状态
	private String contractId;// 订单合同
	private double service_charge;// 手续费
	private double realAmount;// 实际到账
	private long maxLimitTime;// 最大还款日期
	private long createTime;// 创建时间
	private long deliverTime;// 放款时间
	private double realRefundAmount;// 实际还款
	private long refundTime;// 实际还款日期

	public OrderValueObject(Order order) {
		this.id = order.getId();
		this.userId = order.getUserId();
		this.bankCardNo = order.getBankCardNo();
		this.amount = order.getAmount();
		this.dayNum = order.getDayNum();
		this.service_charge_rate = order.getService_charge_rate();
		this.freeTimeOfInterest = order.getFreeTimeOfInterest();
		this.overdue = order.getOverdue();
		this.rate = order.getRate();
		this.overdue_rate = order.getOverdue_rate();
		this.state = order.getState();
		this.contractId = order.getContractId();
		this.service_charge = order.getService_charge();
		this.realAmount = order.getRealAmount();
		this.maxLimitTime = order.getMaxLimitTime();
		this.createTime = order.getCreateTime();
		this.deliverTime = order.getDeliverTime();
		this.realRefundAmount = order.getRealRefundAmount();
		this.refundTime = order.getRefundTime();
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

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getDayNum() {
		return dayNum;
	}

	public void setDayNum(int dayNum) {
		this.dayNum = dayNum;
	}

	public double getService_charge_rate() {
		return service_charge_rate;
	}

	public void setService_charge_rate(double service_charge_rate) {
		this.service_charge_rate = service_charge_rate;
	}

	public long getFreeTimeOfInterest() {
		return freeTimeOfInterest;
	}

	public void setFreeTimeOfInterest(long freeTimeOfInterest) {
		this.freeTimeOfInterest = freeTimeOfInterest;
	}

	public long getOverdue() {
		return overdue;
	}

	public void setOverdue(long overdue) {
		this.overdue = overdue;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getOverdue_rate() {
		return overdue_rate;
	}

	public void setOverdue_rate(double overdue_rate) {
		this.overdue_rate = overdue_rate;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public double getService_charge() {
		return service_charge;
	}

	public void setService_charge(double service_charge) {
		this.service_charge = service_charge;
	}

	public double getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(double realAmount) {
		this.realAmount = realAmount;
	}

	public long getMaxLimitTime() {
		return maxLimitTime;
	}

	public void setMaxLimitTime(long maxLimitTime) {
		this.maxLimitTime = maxLimitTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(long deliverTime) {
		this.deliverTime = deliverTime;
	}

	public double getRealRefundAmount() {
		return realRefundAmount;
	}

	public void setRealRefundAmount(double realRefundAmount) {
		this.realRefundAmount = realRefundAmount;
	}

	public long getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(long refundTime) {
		this.refundTime = refundTime;
	}

}
