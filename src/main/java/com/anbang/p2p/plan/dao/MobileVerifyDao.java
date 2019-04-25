package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.MobileVerify;


public interface MobileVerifyDao {
    void save(MobileVerify mobileVerify);

    MobileVerify getById(String id);

    void updateStateAndData(String id, String state, String report);

}
