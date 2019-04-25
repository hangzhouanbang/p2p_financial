package com.anbang.p2p.cqrs.q.dao;

import java.util.List;

import com.anbang.p2p.cqrs.q.dbo.UserDbo;
import com.anbang.p2p.web.vo.UserQuery;

public interface UserDboDao {

	void save(UserDbo dbo);

	void updateNicknameAndHeadimgurlById(String userId, String nickname, String headimgurl);

	UserDbo findById(String userId);

	UserDbo findByPhone(String phone);

	long getAmount(UserQuery userQuery);

	List<UserDbo> find(int page, int size, UserQuery userQuery);

	void updateIPById(String userId, String loginIp, String ipAddress);

	void updataVerify(String userId, Boolean isVerify, String realName, String IDcard);

	// 更新借款、逾期次数
	void updateCountAndState(String userId, Integer orderCount, Integer overdueCount, String state);

	void updateUserState(String userId, String state);
}
