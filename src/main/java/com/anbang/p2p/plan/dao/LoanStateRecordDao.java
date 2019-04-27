package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.LoanStateRecord;

import java.util.List;

/**
 * @Description:
 */
public interface LoanStateRecordDao {

    void save(LoanStateRecord loanStateRecord);

    List<LoanStateRecord> list(String orderId);
}
