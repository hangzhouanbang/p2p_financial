package com.anbang.p2p.cqrs.q.dao;

import com.anbang.p2p.cqrs.q.dbo.UserDbo;

public interface UserDboDao {

	void save(UserDbo dbo);

	UserDbo findById(String userId);

	UserDbo findByPhone(String phone);
}
