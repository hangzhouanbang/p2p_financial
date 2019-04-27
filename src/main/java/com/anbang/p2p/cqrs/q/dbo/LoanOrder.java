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
	private String payType;
	private String payAccount;
	private String IDcard;	//身份证号码
	private double amount;// 贷款金额
	private int dayNum;// 贷款天数discard
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

	private String repayType; //还款类型
	private String repayAccount;  //还款账户

	private String loginIp;
	private String ipAddress;

	private double interest;	//利息
	private int overdueDay;	//逾期时长
	private double shouldRepayAmount;	//应还金额

	private boolean export;  	// 是否导出

	private double expand_charge; // 延期手续费
	private double expandTotal;	  // 延期总费用
	private int expandTimes;	  // 延期次数
	private String contractPath;

	public LoanOrder() {

	}

	public LoanOrder(OrderValueObject orderValueObject, UserDbo user, OrderContract contract, UserBaseInfo baseInfo) {
		this.id = orderValueObject.getId();
		this.userId = orderValueObject.getUserId();
		this.nickname = user.getNickname();
		this.headimgurl = user.getHeadimgurl();
		this.realName = baseInfo.getRealName();
		this.phone = user.getPhone();
		this.payType = orderValueObject.getPayType();
		this.payAccount = orderValueObject.getPayAccount();
		this.IDcard = baseInfo.getIDcard();
		this.amount = orderValueObject.getAmount();
		this.dayNum = orderValueObject.getDayNum();
		this.freeTimeOfInterest = orderValueObject.getFreeTimeOfInterest();
		this.overdue = orderValueObject.getOverdue();
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

		this.expand_charge = orderValueObject.getExpand_charge();
		this.expandTotal = orderValueObject.getExpandTotal();
		this.expandTimes = orderValueObject.getExpandTimes();
//		this.contractPath = "http://47.91.219.7/base/getWord/" + id + ".docx";		//temp
		this.contractPath = "";		//temp
	}

	/**
	 * 判断当前订单状态
	 */
	public void checkState() {
		if (OrderState.refund.equals(state) || OrderState.overdue.equals(state) || OrderState.collection.equals(state)) {
			long nowTime = System.currentTimeMillis();
			if (nowTime < maxLimitTime) {
				state = OrderState.refund;
				return;
			}
			if (nowTime > (maxLimitTime + overdue)) {
				state = OrderState.collection;
				return;
			}
			state = OrderState.overdue;
		}
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

	public String getIDcard() {
		return IDcard;
	}

	public void setIDcard(String IDcard) {
		this.IDcard = IDcard;
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

	public boolean isExport() {
		return export;
	}

	public void setExport(boolean export) {
		this.export = export;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public int getOverdueDay() {
		return overdueDay;
	}

	public void setOverdueDay(int overdueDay) {
		this.overdueDay = overdueDay;
	}

	public double getShouldRepayAmount() {
		return shouldRepayAmount;
	}

	public void setShouldRepayAmount(double shouldRepayAmount) {
		this.shouldRepayAmount = shouldRepayAmount;
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

	public String getContractPath() {
		return contractPath;
	}

	public void setContractPath(String contractPath) {
		this.contractPath = contractPath;
	}
}
