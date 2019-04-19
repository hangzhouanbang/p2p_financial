package com.anbang.p2p.cqrs.q.dbo;

import com.anbang.p2p.cqrs.c.domain.order.OrderValueObject;

/**
 * 代付流水
 */
public class AgentPayRecord {
	private String id;
	private String loanOrderId;// 卡密
	private String userId;// 用户
	private String nickname;// 用户昵称
	private String headimgurl;// 头像
	private String realName;// 真实姓名
	private String phone;// 手机号码
	private double amount;// 贷款金额
	private double refundAmount;// 还款金额
	private String payType;
	private String payAccount;
	private OrderContract contract;// 订单合同
	private long createTime;// 创建时间

	private String status;

	public AgentPayRecord(OrderValueObject orderValueObject, LoanOrder loanOrder) {
		loanOrderId = orderValueObject.getId();
		amount = orderValueObject.getAmount();
		refundAmount = orderValueObject.getRealRefundAmount();
		createTime = orderValueObject.getRefundTime();
		this.payType = loanOrder.getPayType();
		this.payAccount = loanOrder.getPayAccount();
		this.contract = loanOrder.getContract();
		userId = loanOrder.getUserId();
		nickname = loanOrder.getNickname();
		headimgurl = loanOrder.getHeadimgurl();
		phone = loanOrder.getPhone();
		realName = loanOrder.getRealName();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoanOrderId() {
		return loanOrderId;
	}

	public void setLoanOrderId(String loanOrderId) {
		this.loanOrderId = loanOrderId;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
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

	public OrderContract getContract() {
		return contract;
	}

	public void setContract(OrderContract contract) {
		this.contract = contract;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
