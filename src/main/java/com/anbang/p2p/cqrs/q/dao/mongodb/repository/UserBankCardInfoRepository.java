package com.anbang.p2p.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.p2p.cqrs.q.dbo.UserBankCardInfo;

public interface UserBankCardInfoRepository extends MongoRepository<UserBankCardInfo, String> {

}
