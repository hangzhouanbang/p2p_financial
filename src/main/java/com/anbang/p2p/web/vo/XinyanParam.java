package com.anbang.p2p.web.vo;

/**
 * @Description:
 */
public class XinyanParam {
    private String msg;
    private String taskId;
    private String token;
    private String apiUser;
    private String apiEnc;
    private String apiName;
    private Boolean success;
//    private String type;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApiUser() {
        return apiUser;
    }

    public void setApiUser(String apiUser) {
        this.apiUser = apiUser;
    }

    public String getApiEnc() {
        return apiEnc;
    }

    public void setApiEnc(String apiEnc) {
        this.apiEnc = apiEnc;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "XinyanParam{" +
                "msg='" + msg + '\'' +
                ", taskId='" + taskId + '\'' +
                ", token='" + token + '\'' +
                ", apiUser='" + apiUser + '\'' +
                ", apiEnc='" + apiEnc + '\'' +
                ", apiName='" + apiName + '\'' +
                ", success=" + success +
                '}';
    }
}
