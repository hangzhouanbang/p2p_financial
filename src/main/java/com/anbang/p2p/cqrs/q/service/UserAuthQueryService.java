package com.anbang.p2p.cqrs.q.service;

import java.util.List;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.plan.bean.MobileVerify;
import com.anbang.p2p.plan.bean.OrgInfo;
import com.anbang.p2p.plan.bean.RiskData;
import com.anbang.p2p.plan.bean.ShoppingVerify;
import com.anbang.p2p.plan.dao.MobileVerifyDao;
import com.anbang.p2p.plan.dao.OrgInfoDao;
import com.anbang.p2p.plan.dao.RiskDataDao;
import com.anbang.p2p.plan.dao.ShoppingVerifyDao;
import com.anbang.p2p.web.vo.UserQuery;
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

	@Autowired
	private MobileVerifyDao mobileVerifyDao;

	@Autowired
	private OrgInfoDao orgInfoDao;

	@Autowired
	private ShoppingVerifyDao shoppingVerifyDao;

	@Autowired
	private RiskDataDao riskDataDao;

	/**
	 * 创建用户并授权
	 */
	public void createUserAndAddThirdAuth(String userId, String publisher, String uuid, String loginIp, String ipAddress) {
		UserDbo user = new UserDbo();
		user.setId(userId);
		user.setNickname("");
		user.setHeadimgurl("");
		user.setPhone(uuid);
		user.setLoginIp(loginIp);
		user.setIpAddress(ipAddress);
		user.setCreateTime(System.currentTimeMillis());
		user.setState(OrderState.init.name());
		user.setVerify(false);
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

	// 更新借款、逾期次数
	public void updateCountAndState(String userId, Integer orderCount, Integer overdueCount, String state){
		userDboDao.updateCountAndState(userId, orderCount, overdueCount, state);
	}

	public void updateNicknameAndHeadimgurlById(String userId, String nickname, String headimgurl) {
		userDboDao.updateNicknameAndHeadimgurlById(userId, nickname, headimgurl);
	}

	public ListPage findUserDbo(int page, int size, UserQuery userQuery) {
		int amount = (int) userDboDao.getAmount(userQuery);
		List<UserDbo> userList = userDboDao.find(page, size, userQuery);
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

	public void updateIPById(String userId, String loginIp, String ipAddress){
		userDboDao.updateIPById(userId, loginIp, ipAddress);
	}

	public void saveMobileVerify(MobileVerify mobileVerify) {
		mobileVerifyDao.save(mobileVerify);
	}

	public MobileVerify getMobileVerify(String id) {
		return mobileVerifyDao.getById(id);
	}

	public void updateStateAndData(String id, String state, String report) {
		mobileVerifyDao.updateStateAndData(id, state, report);
	}

	public OrgInfo getOrgInfo(String id) {
		return orgInfoDao.getById(id);
	}

	public ShoppingVerify getShoppingVerify(String id) {
		return shoppingVerifyDao.getById(id);
	}

	public void saveShoppingVerify(ShoppingVerify shoppingVerify) {
		 shoppingVerifyDao.save(shoppingVerify);
	}

	public void saveRiskData(RiskData riskData) {
		riskDataDao.save(riskData);
	}

	public RiskData getRiskData(String id) {
		return riskDataDao.getById(id);
	}
}
