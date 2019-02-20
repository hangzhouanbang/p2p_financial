package com.anbang.p2p.cqrs.c.domain.order;

import java.math.BigDecimal;

public class Order {
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

	/**
	 * 计算实际到账金额:本金-本金X手续费比例
	 */
	public void calculateRealAmount() {
		// 如果需要精确计算，非要用String来够造BigDecimal不可
		BigDecimal b_amount = new BigDecimal(Double.toString(amount));
		BigDecimal b_service_charge = new BigDecimal(Double.toString(service_charge));
		realAmount = b_amount.subtract(b_amount.multiply(b_service_charge)).doubleValue();
	}

	/**
	 * 计算应还款
	 */
	public double calculateRefundAmount(long currentTime) throws IllegalOperationException {
		if (currentTime < deliverTime) {
			throw new IllegalOperationException();
		}
		// 如果需要精确计算，非要用String来够造BigDecimal不可
		BigDecimal b_amount = new BigDecimal(Double.toString(amount));
		BigDecimal b_rate = new BigDecimal(Double.toString(rate));
		BigDecimal b_freeOfInterest = new BigDecimal(Integer.toString(freeOfInterest));
		if (currentTime > maxLimitTime) {
			// 逾期应还:到期应还金额+逾期天数X本金X逾期利率
			long limitDay = (maxLimitTime - deliverTime) / 1000 / 60 / 60 / 24;
			BigDecimal b_limitDay = new BigDecimal(Long.toString(limitDay));
			BigDecimal limit_num = b_limitDay.subtract(b_freeOfInterest);
			BigDecimal amount = b_amount.add(b_amount.multiply(b_rate.multiply(limit_num)));

			long day = (currentTime - maxLimitTime) / 1000 / 60 / 60 / 24;
			BigDecimal b_day = new BigDecimal(Long.toString(day));
			BigDecimal b_overdue = new BigDecimal(Double.toString(overdue));
			return amount.add(b_amount.multiply(b_overdue.multiply(b_day))).doubleValue();
		} else {
			// 到期应还:本金X相应借款天数利率X（借款天数-免息天数）+本金
			long day = (currentTime - deliverTime) / 1000 / 60 / 60 / 24;
			BigDecimal b_day = new BigDecimal(Long.toString(day));
			BigDecimal num = b_day.subtract(b_freeOfInterest);
			return b_amount.add(b_amount.multiply(b_rate.multiply(num))).doubleValue();
		}
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
