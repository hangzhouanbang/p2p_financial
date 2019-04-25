package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.OrgInfo;

/**
 * @Description:
 */
public interface OrgInfoDao {
    void save(OrgInfo orgInfo);

    OrgInfo getById(String id);
}
