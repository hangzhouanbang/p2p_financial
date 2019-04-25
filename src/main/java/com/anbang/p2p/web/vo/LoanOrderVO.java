package com.anbang.p2p.web.vo;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.q.dbo.LoanOrder;
import com.anbang.p2p.cqrs.q.dbo.OrderContract;

public class LoanOrderVO {
	private String id;// 卡密
	private String userId;// 用户
	private String nickname;// 用户昵称
	private String headimgurl;// 头像
	private String realName;// 真实姓名
	private String phone;// 手机号码
	private String payType;
	private String payAccount;
	private String IDcard;	//身份证号码
	private double amount;// 贷款金额
	private int dayNum;// 贷款天数
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
	private String loginIp;
	private String ipAddress;
//	private double realRefundAmount;// 实际还款
//	private long refundTime;// 实际还款日期
//	private String repayType; //还款类型
//	private String repayAccount;  //还款账户
//	private boolean export;  	// 是否导出

	private double interest;	//利息
	private long overdueTime;	//逾期时长
	private double shouldRepayAmount;	//应还金额

	public LoanOrderVO() {
	}

	public LoanOrderVO(LoanOrder loanOrder) {
		this.id = loanOrder.getId();
		this.userId = loanOrder.getUserId();
		this.nickname = loanOrder.getNickname();
		this.headimgurl = loanOrder.getHeadimgurl();
		this.realName = loanOrder.getRealName();
		this.phone = loanOrder.getPhone();
		this.payType = loanOrder.getPayType();
		this.payAccount = loanOrder.getPayAccount();
		this.amount = loanOrder.getAmount();
		this.dayNum = loanOrder.getDayNum();
		this.freeTimeOfInterest = loanOrder.getFreeTimeOfInterest();
		this.overdue = loanOrder.getOverdue();
		this.rate = loanOrder.getRate();
		this.overdue_rate = loanOrder.getOverdue_rate();
		this.state = loanOrder.getState();
		this.contract = loanOrder.getContract();
		this.service_charge = loanOrder.getService_charge();
		this.realAmount = loanOrder.getRealAmount();
		this.maxLimitTime = loanOrder.getMaxLimitTime();
		this.createTime = loanOrder.getCreateTime();
		this.IDcard = loanOrder.getIDcard();
		this.deliverTime = loanOrder.getDeliverTime();
		this.loginIp = loanOrder.getLoginIp();
		this.ipAddress = loanOrder.getIpAddress();


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

	public String getIDcard() {
		return IDcard;
	}

	public void setIDcard(String IDcard) {
		this.IDcard = IDcard;
	}

	public long getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(long deliverTime) {
		this.deliverTime = deliverTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public long getOverdueTime() {
		return overdueTime;
	}

	public void setOverdueTime(long overdueTime) {
		this.overdueTime = overdueTime;
	}

	public double getShouldRepayAmount() {
		return shouldRepayAmount;
	}

	public void setShouldRepayAmount(double shouldRepayAmount) {
		this.shouldRepayAmount = shouldRepayAmount;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}
}
