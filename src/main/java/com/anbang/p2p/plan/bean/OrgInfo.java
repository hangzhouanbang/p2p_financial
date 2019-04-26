package com.anbang.p2p.plan.bean;

/**
 * 机构信息
 */
public class OrgInfo {
    private String id;
    private String orgName;  // 机构名称
    private String phone;    // 机构电话
    private String servicePhone;    // 客服电话
    private String serviceQQ;       // 客服qq
    private String serviceWX;       // 客服微信

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getServiceQQ() {
        return serviceQQ;
    }

    public void setServiceQQ(String serviceQQ) {
        this.serviceQQ = serviceQQ;
    }

    public String getServiceWX() {
        return serviceWX;
    }

    public void setServiceWX(String serviceWX) {
        this.serviceWX = serviceWX;
    }
}
