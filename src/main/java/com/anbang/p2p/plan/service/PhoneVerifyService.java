package com.anbang.p2p.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.p2p.plan.bean.UserVerifyPhoneInfo;
import com.anbang.p2p.plan.dao.UserVerifyPhoneInfoDao;

@Service
public class PhoneVerifyService {

	@Autowired
	private UserVerifyPhoneInfoDao userVerifyPhoneInfoDao;

	public void saveVerifyPhoneCode(UserVerifyPhoneInfo info) {
		userVerifyPhoneInfoDao.save(info);
	}

	public UserVerifyPhoneInfo findUserVerifyPhoneInfoByPhone(String phone) {
		return userVerifyPhoneInfoDao.findById(phone);
	}

}
