package com.anbang.p2p.plan.bean;

import java.util.List;

/**
 * 销账记录
 */
public class ImportRecord {
    private String id;
    private String name;        //文件名称
    private List<RepayRecord> repayRecords;     //销账数据
    private Integer recordCount;
    private ImportState importState;    //状态
    private String causeBy;     // 原因
    private long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RepayRecord> getRepayRecords() {
        return repayRecords;
    }

    public void setRepayRecords(List<RepayRecord> repayRecords) {
        this.repayRecords = repayRecords;
    }

    public ImportState getImportState() {
        return importState;
    }

    public void setImportState(ImportState importState) {
        this.importState = importState;
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

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }
}
