package com.anbang.p2p.cqrs.q.dao;

import java.util.List;

import com.anbang.p2p.cqrs.q.dbo.UserDbo;

public interface UserDboDao {

	void save(UserDbo dbo);

	void updateNicknameAndHeadimgurlById(String userId, String nickname, String headimgurl);

	UserDbo findById(String userId);

	UserDbo findByPhone(String phone);

	long getAmount();

	List<UserDbo> find(int page, int size);

	void updateIPById(String userId, String loginIp, String ipAddress);

	void updataVerify(String userId, Boolean isVerify, String realName);

	// 更新借款、逾期次数
	void updateCount(String userId, Integer orderCount, Integer overdueCount);
}
