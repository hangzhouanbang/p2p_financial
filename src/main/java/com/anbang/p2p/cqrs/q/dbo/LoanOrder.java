package com.anbang.p2p.cqrs.q.dbo;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;

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
	private String bankCardNo;// 银行卡号
	private double amount;// 贷款金额
	private int dayNum;// 贷款天数
	private double service_charge_rate;// 手续费比例
	private long freeTimeOfInterest;// 免息时间
	private long overdue;// 逾期转催收时间
	private double rate;// 每日利率
	private double overdue_rate;// 逾期利率
	private OrderState state;// 卡密状态
	private OrderContract contract;// 订单合同
	private double service_charge;// 手续费
	private double realAmount;// 实际到账
	private long maxLimitTime;// 最大还款日期
	private long createTime;// 创建时间
	private long deliverTime;// 放款时间
	private double realRefundAmount;// 实际还款
	private long refundTime;// 实际还款日期

	public LoanOrder() {

	}

	public LoanOrder(OrderValueObject orderValueObject, UserDbo user, OrderContract contract, UserBaseInfo baseInfo) {
		this.id = orderValueObject.getId();
		this.userId = orderValueObject.getUserId();
		this.nickname = user.getNickname();
		this.headimgurl = user.getHeadimgurl();
		this.realName = baseInfo.getRealName();
		this.phone = user.getPhone();
		this.bankCardNo = orderValueObject.getBankCardNo();
		this.amount = orderValueObject.getAmount();
		this.dayNum = orderValueObject.getDayNum();
		this.service_charge_rate = orderValueObject.getService_charge_rate();
		this.freeTimeOfInterest = orderValueObject.getFreeTimeOfInterest();
		this.overdue = orderValueObject.getOverdue();
		this.rate = orderValueObject.getRate();
		this.overdue_rate = orderValueObject.getOverdue_rate();
		this.state = orderValueObject.getState();
		this.contract = contract;
		this.service_charge = orderValueObject.getService_charge();
		this.realAmount = orderValueObject.getRealAmount();
		this.maxLimitTime = orderValueObject.getMaxLimitTime();
		this.createTime = orderValueObject.getCreateTime();
		this.deliverTime = orderValueObject.getDeliverTime();
		this.realRefundAmount = orderValueObject.getRealRefundAmount();
		this.refundTime = orderValueObject.getRefundTime();
	}

	/**
	 * 是否逾期
	 */
	public boolean isOverdue(long currentTime) {
		if (currentTime > maxLimitTime) {
			return true;
		}
		return false;
	}

	/**
	 * 是否催收
	 */
	public boolean isCuishou(long currentTime) {
		if (currentTime > maxLimitTime + overdue) {
			return true;
		}
		return false;
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

	public OrderContract getContract() {
		return contract;
	}

	public void setContract(OrderContract contract) {
		this.contract = contract;
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