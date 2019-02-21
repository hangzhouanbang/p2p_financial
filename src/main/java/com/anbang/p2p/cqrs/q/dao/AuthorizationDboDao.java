package com.anbang.p2p.cqrs.q.dao;

import com.anbang.p2p.cqrs.q.dbo.AuthorizationDbo;

public interface AuthorizationDboDao {

	AuthorizationDbo find(boolean thirdAuth, String publisher, String uuid);

	void save(AuthorizationDbo authDbo);
}
