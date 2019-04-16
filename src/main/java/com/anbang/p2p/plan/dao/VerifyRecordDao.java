package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.VerifyRecord;

public interface VerifyRecordDao {
    void save(VerifyRecord verifyRecord);

    VerifyRecord getById(String id);

    VerifyRecord getByUerId(String uerId);

    void updateStateAndCause(String id, String state, String result, String causeBy);
}
