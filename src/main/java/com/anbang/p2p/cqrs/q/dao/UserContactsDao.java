package com.anbang.p2p.cqrs.q.dao;

import com.anbang.p2p.cqrs.q.dbo.UserContacts;

public interface UserContactsDao {

	void save(UserContacts contacts);

	UserContacts findById(String id);

	UserContacts findByUserId(String userId);
}
