package com.anbang.p2p.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.p2p.cqrs.q.dao.AuthorizationDboDao;
import com.anbang.p2p.cqrs.q.dao.UserAgentInfoDao;
import com.anbang.p2p.cqrs.q.dao.UserBankCardInfoDao;
import com.anbang.p2p.cqrs.q.dao.UserBaseInfoDao;
import com.anbang.p2p.cqrs.q.dao.UserContactsDao;
import com.anbang.p2p.cqrs.q.dao.UserCreditInfoDao;
import com.anbang.p2p.cqrs.q.dao.UserDboDao;
import com.anbang.p2p.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.p2p.cqrs.q.dbo.UserAgentInfo;
import com.anbang.p2p.cqrs.q.dbo.UserBankCardInfo;
import com.anbang.p2p.cqrs.q.dbo.UserBaseInfo;
import com.anbang.p2p.cqrs.q.dbo.UserContacts;
import com.anbang.p2p.cqrs.q.dbo.UserCreditInfo;
import com.anbang.p2p.cqrs.q.dbo.UserDbo;
import com.highto.framework.web.page.ListPage;

@Service
public class UserAuthQueryService {

	@Autowired
	private AuthorizationDboDao authorizationDboDao;

	@Autowired
	private UserBaseInfoDao userBaseInfoDao;

	@Autowired
	private UserDboDao userDboDao;

	@Autowired
	private UserAgentInfoDao userAgentInfoDao;

	@Autowired
	private UserContactsDao userContactsDao;

	@Autowired
	private UserCreditInfoDao userCreditInfoDao;

	@Autowired
	private UserBankCardInfoDao userBankCardInfoDao;

	/**
	 * 创建用户并授权
	 */
	public void createUserAndAddThirdAuth(String userId, String publisher, String uuid) {
		UserDbo user = new UserDbo();
		user.setId(userId);
		user.setNickname("");
		user.setHeadimgurl("");
		user.setPhone(uuid);
		userDboDao.save(user);

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

	public UserDbo findUserDboByPhone(String phone) {
		return userDboDao.findByPhone(phone);
	}

	public UserDbo findUserDboByUserId(String userId) {
		return userDboDao.findById(userId);
	}

	public void updateNicknameAndHeadimgurlById(String userId, String nickname, String headimgurl) {
		userDboDao.updateNicknameAndHeadimgurlById(userId, nickname, headimgurl);
	}

	public ListPage findUserDbo(int page, int size) {
		int amount = (int) userDboDao.getAmount();
		List<UserDbo> userList = userDboDao.find(page, size);
		return new ListPage(userList, page, size, amount);
	}

	public UserBaseInfo findUserBaseInfoByUserId(String userId) {
		return userBaseInfoDao.findById(userId);
	}

	public void saveUserBaseInfo(UserBaseInfo info) {
		userBaseInfoDao.save(info);
	}

	public UserAgentInfo findUserAgentInfoByUserId(String userId) {
		return userAgentInfoDao.findById(userId);
	}

	public void saveUserAgentInfo(UserAgentInfo info) {
		userAgentInfoDao.save(info);
	}

	public UserContacts findUserContactsByUserId(String userId) {
		return userContactsDao.findByUserId(userId);
	}

	public void saveContacts(UserContacts contacts) {
		userContactsDao.save(contacts);
	}

	public UserCreditInfo findUserCreditInfoByUserId(String userId) {
		return userCreditInfoDao.findById(userId);
	}

	public void saveUserCreditInfo(UserCreditInfo info) {
		userCreditInfoDao.save(info);
	}

	public ListPage findBankCardInfoByUserId(int page, int size, String userId) {
		int amount = (int) userBankCardInfoDao.getAmountByUserId(userId);
		List<UserBankCardInfo> infoList = userBankCardInfoDao.findByUserId(page, size, userId);
		return new ListPage(infoList, page, size, amount);
	}

	public void saveBankCardInfo(UserBankCardInfo info) {
		userBankCardInfoDao.save(info);
	}

	public UserBankCardInfo findUserBankCardInfoById(String cardId) {
		return userBankCardInfoDao.findById(cardId);
	}

	public long getAmountByUserId(String userId) {
		return userBankCardInfoDao.getAmountByUserId(userId);
	}

	public UserBankCardInfo findById(String userId) {
		return userBankCardInfoDao.findById(userId);
	}
}
