package com.anbang.p2p.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.p2p.plan.bean.UserVerifyPhoneInfo;

public interface UserVerifyPhoneInfoRepository extends MongoRepository<UserVerifyPhoneInfo, String> {

}
