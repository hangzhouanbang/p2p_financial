package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.RiskInfo;

public interface RiskInfoDao {
    void save(RiskInfo riskInfo);

    RiskInfo getById(String id);

    RiskInfo getByUserId(String userId);
}
