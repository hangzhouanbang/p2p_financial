package com.anbang.p2p.plan.bean;

/**
 * 销账导入
 */
public class RepayRecord {
    private String id;
    private String userId;
    private String realName;
    private double amount;
    private double repayAmount;  //销账金额
    private RepayRecordState state;


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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(double repayAmount) {
        this.repayAmount = repayAmount;
    }

    public RepayRecordState getState() {
        return state;
    }

    public void setState(RepayRecordState state) {
        this.state = state;
    }
}
