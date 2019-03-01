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
}
