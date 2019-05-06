package com.anbang.p2p.plan.bean;

/**
 * @Description: 风控数据（新颜）
 */
public class RiskData {
    private String id;
    private String leidaId;    // 雷达trans_id
    private String leidaJson;  // 新颜全景雷达
    private String tanzhenId;    // 探针trans_id
    private String tanzhenJson;  // 新颜探针A

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeidaJson() {
        return leidaJson;
    }

    public void setLeidaJson(String leidaJson) {
        this.leidaJson = leidaJson;
    }

    public String getTanzhenJson() {
        return tanzhenJson;
    }

    public void setTanzhenJson(String tanzhenJson) {
        this.tanzhenJson = tanzhenJson;
    }

    public String getLeidaId() {
        return leidaId;
    }

    public void setLeidaId(String leidaId) {
        this.leidaId = leidaId;
    }

    public String getTanzhenId() {
        return tanzhenId;
    }

    public void setTanzhenId(String tanzhenId) {
        this.tanzhenId = tanzhenId;
    }
}
