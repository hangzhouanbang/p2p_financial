package com.anbang.p2p.cqrs.q.dbo;

/**
 * 还款信息
 */
public class RefundInfo {
	private String id;
	private String loanOrderId;// 卡密
	private String userId;// 用户
	private String nickname;// 用户昵称
	private String headimgurl;// 头像
	private String realName;// 真实姓名
	private String phone;// 手机号码
	private double amount;// 贷款金额
	private double refundAmount;// 还款金额
	private String repayType;
	private String repayAccount;
	private OrderContract contract;// 订单合同
	private long createTime;// 创建时间

	private String status;

	private double expand_charge;	//延期手续费
	private String paymentType;  // 付款类型:还款、延期

	public RefundInfo() {
	}

	public RefundInfo(LoanOrder loanOrder, String repayType, double refundAmount) {
		loanOrderId = loanOrder.getId();
		amount = loanOrder.getAmount();
		this.refundAmount = refundAmount;
		createTime = System.currentTimeMillis();
		this.repayType = repayType;
		this.contract = loanOrder.getContract();
		userId = loanOrder.getUserId();
		nickname = loanOrder.getNickname();
		headimgurl = loanOrder.getHeadimgurl();
		phone = loanOrder.getPhone();
		realName = loanOrder.getRealName();
		expand_charge = loanOrder.getExpand_charge();
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

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getRepayAccount() {
		return repayAccount;
	}

	public void setRepayAccount(String repayAccount) {
		this.repayAccount = repayAccount;
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

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public double getExpand_charge() {
		return expand_charge;
	}

	public void setExpand_charge(double expand_charge) {
		this.expand_charge = expand_charge;
	}
}
