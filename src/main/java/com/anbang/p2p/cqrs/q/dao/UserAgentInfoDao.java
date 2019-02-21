package com.anbang.p2p.cqrs.q.dao;

import com.anbang.p2p.cqrs.q.dbo.UserAgentInfo;

public interface UserAgentInfoDao {

	void save(UserAgentInfo info);
}
