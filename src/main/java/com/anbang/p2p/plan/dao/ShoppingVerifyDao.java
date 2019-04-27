package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.ShoppingVerify;

public interface ShoppingVerifyDao {
    void save(ShoppingVerify shoppingVerify);

    ShoppingVerify getById(String id);

    void updateStateAndData(String id, String state, String report);
}
