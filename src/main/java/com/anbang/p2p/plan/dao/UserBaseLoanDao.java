package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.UserBaseLoan;

public interface UserBaseLoanDao {

	void save(UserBaseLoan loan);

	UserBaseLoan findByUserId(String userId);
}
