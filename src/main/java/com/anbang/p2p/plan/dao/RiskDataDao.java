package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.RiskData;

public interface RiskDataDao {
    void save(RiskData riskData);

    RiskData getById(String id);
}
