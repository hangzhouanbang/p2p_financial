package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.VerifyRecord;

public interface VerifyRecordDao {
    void save(VerifyRecord verifyRecord);

    VerifyRecord getById(String id);

    VerifyRecord getByUerId(String uerId);
}
