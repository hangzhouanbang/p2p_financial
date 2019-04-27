package com.anbang.p2p.plan.bean;

import com.anbang.p2p.constants.Operator;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Description: loanorder 的状态记录
 */
@Document
public class LoanStateRecord {
    private String id;
    @Indexed(unique = false)
    private String orderId;  // 卡密id
    private String toState; // 进入状态
    private String desc;    // 描述
    private Operator operator;  // 操作人
    private double amount;
    private long createTime;

    public LoanStateRecord() {
    }

    public LoanStateRecord(String id, String orderId, String toState, String desc, Operator operator, String createTime) {
        this.id = id;
        this.orderId = orderId;
        this.toState = toState;
        this.desc = desc;
        this.operator = operator;
        this.createTime = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
