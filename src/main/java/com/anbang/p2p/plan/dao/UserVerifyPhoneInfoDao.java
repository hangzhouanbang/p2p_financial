package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.UserVerifyPhoneInfo;

public interface UserVerifyPhoneInfoDao {

	void save(UserVerifyPhoneInfo info);

	UserVerifyPhoneInfo findById(String phone);
}
