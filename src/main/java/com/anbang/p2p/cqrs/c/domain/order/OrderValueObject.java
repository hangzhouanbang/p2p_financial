package com.anbang.p2p.cqrs.c.domain.order;

public class OrderValueObject {
	private String id;// 卡密
	private String userId;// 用户
	private String payType;
	private String payAccount;
	private double amount;// 贷款金额
	private int dayNum;// 贷款天数
	private long freeTimeOfInterest;// 免息时间
	private long overdue;// 逾期转催收时间
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

	private double expand_charge; // 延期手续费
	private double expandTotal;	  // 延期总费用
	private int expandTimes;	  // 延期次数

	public OrderValueObject(Order order) {
		this.id = order.getId();
		this.userId = order.getUserId();
		this.payType = order.getPayType();
		this.payAccount = order.getPayAccount();
		this.amount = order.getAmount();
		this.dayNum = order.getDayNum();
		this.freeTimeOfInterest = order.getFreeTimeOfInterest();
		this.overdue = order.getOverdue();
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
		this.expand_charge = order.getExpand_charge();
		this.expandTotal = order.getExpandTotal();
		this.expandTimes = order.getExpandTimes();
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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
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

	public double getExpand_charge() {
		return expand_charge;
	}

	public void setExpand_charge(double expand_charge) {
		this.expand_charge = expand_charge;
	}

	public double getExpandTotal() {
		return expandTotal;
	}

	public void setExpandTotal(double expandTotal) {
		this.expandTotal = expandTotal;
	}

	public int getExpandTimes() {
		return expandTimes;
	}

	public void setExpandTimes(int expandTimes) {
		this.expandTimes = expandTimes;
	}
}
