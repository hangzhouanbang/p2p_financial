package com.anbang.p2p.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.plan.bean.UserVerifyPhoneInfo;
import com.anbang.p2p.plan.dao.UserVerifyPhoneInfoDao;
import com.anbang.p2p.plan.dao.mongodb.repository.UserVerifyPhoneInfoRepository;

@Component
public class MongodbUserVerifyPhoneInfoDao implements UserVerifyPhoneInfoDao {

	@Autowired
	private UserVerifyPhoneInfoRepository userVerifyPhoneInfoRepository;

	@Override
	public void save(UserVerifyPhoneInfo info) {
		userVerifyPhoneInfoRepository.save(info);
	}

	@Override
	public UserVerifyPhoneInfo findById(String phone) {
		return userVerifyPhoneInfoRepository.findOne(phone);
	}

}
