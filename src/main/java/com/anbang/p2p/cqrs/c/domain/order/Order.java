package com.anbang.p2p.cqrs.c.domain.order;

import java.math.BigDecimal;

import com.anbang.p2p.cqrs.c.domain.IllegalOperationException;

/**
 * 卡密
 */
public class Order {
	private String id;// 卡密
	private String userId;// 用户
	private String payType; //用户收款类型
	private String payAccount; //用户收款账户
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


	/**
	 * 计算手续费：用户借款金额，实际到账金额是扣除手续费后的金额
	 */
//	public void calculateServiceCharge() {
//		BigDecimal b_amount = new BigDecimal(Double.toString(amount));
//		BigDecimal b_service_charge_rate = new BigDecimal(Double.toString(service_charge_rate));
//		service_charge = b_amount.multiply(b_service_charge_rate).doubleValue();
//	}

	/**
	 * 计算实际到账金额:本金-手续费
	 */
	public void calculateRealAmount() {
		// 如果需要精确计算，非要用String来够造BigDecimal不可
		BigDecimal b_amount = new BigDecimal(Double.toString(amount));
		BigDecimal b_service_charge = new BigDecimal(Double.toString(service_charge));
		realAmount = b_amount.subtract(b_service_charge).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
	}

	/**
	 * 计算最大还款日期
	 */
	public void calculateMaxLimitTime() {
		maxLimitTime = freeTimeOfInterest + createTime;
	}

	/**
	 * 计算应还款
	 */
//	public double calculateRefundAmount(long currentTime) throws IllegalOperationException {
//		if (currentTime < deliverTime) {
//			throw new IllegalOperationException();
//		}
//		// 如果需要精确计算，非要用String来够造BigDecimal不可
//		BigDecimal b_amount = new BigDecimal(Double.toString(amount));
//		BigDecimal b_rate = new BigDecimal(Double.toString(0));
//		BigDecimal b_freeOfInterest = new BigDecimal(Long.toString(freeTimeOfInterest / 24 / 60 / 60 / 1000));
//		if (currentTime > maxLimitTime) {
//			// 逾期应还:到期应还金额+逾期天数X本金X逾期利率
//			long limitDay = (maxLimitTime - deliverTime) / 1000 / 60 / 60 / 24;
//			BigDecimal b_limitDay = new BigDecimal(Long.toString(limitDay));
//			BigDecimal limit_num = b_limitDay.subtract(b_freeOfInterest);
//			BigDecimal amount = b_amount.add(b_amount.multiply(b_rate.multiply(limit_num)));
//
//			long day = (currentTime - maxLimitTime) / 1000 / 60 / 60 / 24;
//			BigDecimal b_day = new BigDecimal(Long.toString(day));
//			BigDecimal b_overdue_rate = new BigDecimal(Double.toString(overdue_rate));
//			return amount.add(b_amount.multiply(b_overdue_rate.multiply(b_day))).doubleValue();
//		} else {
//			// 到期应还:本金X相应借款天数利率X（借款天数-免息天数）+本金
//			long day = (currentTime - deliverTime) / 1000 / 60 / 60 / 24;
//			BigDecimal b_day = new BigDecimal(Long.toString(day));
//			BigDecimal num = b_day.subtract(b_freeOfInterest);
//
//			// 小于免息时间，则还本金
//			if (b_day.compareTo(b_freeOfInterest) == -1 ) {
//				return b_amount.doubleValue();
//			}
//			return b_amount.add(b_amount.multiply(b_rate.multiply(num))).doubleValue();
//		}
//	}

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
