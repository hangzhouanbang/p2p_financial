package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.ImportRecord;

import java.util.List;

/**
 * @Description:
 */
public interface ImportRecordDao {
    void save(ImportRecord order);

    void delelte(String id);

    ImportRecord getById(String id);

    long getAmount(ImportRecord importRecord);

    List<ImportRecord> findByBean(int page, int size, ImportRecord importRecord);
}
