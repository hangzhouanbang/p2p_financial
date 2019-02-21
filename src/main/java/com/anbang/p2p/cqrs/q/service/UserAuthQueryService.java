package com.anbang.p2p.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.p2p.cqrs.q.dao.AuthorizationDboDao;
import com.anbang.p2p.cqrs.q.dao.UserBaseInfoDao;
import com.anbang.p2p.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;

@Service
public class UserAuthQueryService {

	@Autowired
	private AuthorizationDboDao authorizationDboDao;

	@Autowired
	private UserBaseInfoDao userBaseInfoDao;

	/**
	 * 创建用户并授权
	 */
	public void createUserAndAddThirdAuth(String userId, String publisher, String uuid) {
		UserBaseInfo baseInfo = new UserBaseInfo();
		baseInfo.setId(userId);
		userBaseInfoDao.save(baseInfo);

		AuthorizationDbo authDbo = new AuthorizationDbo();
		authDbo.setUserId(userId);
		authDbo.setPublisher(publisher);
		authDbo.setUuid(uuid);
		authDbo.setThirdAuth(true);
		authorizationDboDao.save(authDbo);
	}

	public AuthorizationDbo findAuthorizationDbo(String publisher, String uuid) {
		return authorizationDboDao.find(true, publisher, uuid);
	}

	public UserBaseInfo findUserBaseInfoByPhone(String phone) {
		return userBaseInfoDao.findByPhone(phone);
	}
}
