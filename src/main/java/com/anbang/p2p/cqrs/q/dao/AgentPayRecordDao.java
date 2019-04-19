package com.anbang.p2p.cqrs.q.dao;

import com.anbang.p2p.cqrs.q.dbo.AgentPayRecord;

import java.util.List;

public interface AgentPayRecordDao {
    void save(AgentPayRecord info);

    long getAmountByUserId(String userId);

    List<AgentPayRecord> findByUserId(int page, int size, String userId);

    void updataStatus(String id, String status);

}
