package com.anbang.p2p.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.p2p.plan.bean.IDCardVerifyInfo;

public interface IDCardVerifyInfoRepository extends MongoRepository<IDCardVerifyInfo, String> {

	IDCardVerifyInfo findOneByBiz_token(String biz_token);
}
