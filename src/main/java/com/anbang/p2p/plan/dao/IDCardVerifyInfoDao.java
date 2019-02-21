package com.anbang.p2p.plan.dao;

import com.anbang.p2p.plan.bean.IDCardVerifyInfo;

public interface IDCardVerifyInfoDao {

	void save(IDCardVerifyInfo info);

	IDCardVerifyInfo findById(String userId);

	IDCardVerifyInfo findByBiz_token(String biz_token);
}
