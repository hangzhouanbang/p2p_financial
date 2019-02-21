package com.anbang.p2p.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.plan.bean.IDCardVerifyInfo;
import com.anbang.p2p.plan.dao.IDCardVerifyInfoDao;
import com.anbang.p2p.plan.dao.mongodb.repository.IDCardVerifyInfoRepository;

@Component
public class MongodbIDCardVerifyInfoDao implements IDCardVerifyInfoDao {

	@Autowired
	private IDCardVerifyInfoRepository repository;

	@Override
	public void save(IDCardVerifyInfo info) {
		repository.save(info);
	}

	@Override
	public IDCardVerifyInfo findById(String userId) {
		return repository.findOne(userId);
	}

	@Override
	public IDCardVerifyInfo findByBiz_token(String biz_token) {
		return repository.findOneByBiz_token(biz_token);
	}

}
