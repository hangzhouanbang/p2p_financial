package com.anbang.p2p.plan.bean;


import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Description: 第三方api的校验记录
 */
@Document
public class VerifyRecord {
    private String id;
    @Indexed(unique = false)
    private String uerId;   // 用户id
    private String state;   // 校验状态
    private String result;  // api返回
    private String causeBy; // 当前状态的原因
    private long createTime;    //创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUerId() {
        return uerId;
    }

    public void setUerId(String uerId) {
        this.uerId = uerId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCauseBy() {
        return causeBy;
    }

    public void setCauseBy(String causeBy) {
        this.causeBy = causeBy;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
