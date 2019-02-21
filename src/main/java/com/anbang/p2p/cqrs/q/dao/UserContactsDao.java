package com.anbang.p2p.cqrs.q.dao;

import com.anbang.p2p.cqrs.q.dbo.UserContacts;

public interface UserContactsDao {

	void save(UserContacts contacts);
}
