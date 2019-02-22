package com.anbang.p2p.cqrs.q.dbo;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;

/**
 * 贷款订单
 */
public class LoanOrder {
	private String id;// 卡密
	private String userId;// 用户
	private String nickname;// 用户昵称
	private String headimgurl;// 头像
	private String realName;// 真实姓名
	private String phone;// 手机号码
	private String address;// 地址
	private double amount;// 贷款金额
	private String bankCardId;// 银行卡号
	private double service_charge;// 手续费
	private long freeOfInterest;// 免息天数
	private long maxLimitTime;// 最大还款日期
	private double rate;// 每日利率
	private double overdue;// 逾期利率
	private OrderState state;// 卡密状态
	private long createTime;// 创建时间
	private long refundTime;// 实际还款日期
	private double refundAmount;// 应还款
	private double realRefundAmount;// 实际还款
	private OrderContract contract;// 订单合同
	private long deliverTime;// 放款日期

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(String bankCardId) {
		this.bankCardId = bankCardId;
	}

	public double getService_charge() {
		return service_charge;
	}

	public void setService_charge(double service_charge) {
		this.service_charge = service_charge;
	}

	public long getFreeOfInterest() {
		return freeOfInterest;
	}

	public void setFreeOfInterest(long freeOfInterest) {
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

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public double getRealRefundAmount() {
		return realRefundAmount;
	}

	public void setRealRefundAmount(double realRefundAmount) {
		this.realRefundAmount = realRefundAmount;
	}

	public OrderContract getContract() {
		return contract;
	}

	public void setContract(OrderContract contract) {
		this.contract = contract;
	}

	public long getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(long deliverTime) {
		this.deliverTime = deliverTime;
	}

}
