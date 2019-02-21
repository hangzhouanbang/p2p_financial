package com.anbang.p2p.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.p2p.plan.bean.IDCardVerifyInfo;
import com.anbang.p2p.plan.dao.IDCardVerifyInfoDao;

@Service
public class BaseVerifyService {

	@Autowired
	private IDCardVerifyInfoDao cardVerifyInfoDao;

	public void saveIDCardVerifyInfo(IDCardVerifyInfo info) {
		cardVerifyInfoDao.save(info);
	}

	public IDCardVerifyInfo findIDCardVerifyInfoByUserId(String userId) {
		return cardVerifyInfoDao.findById(userId);
	}
}
