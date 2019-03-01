package com.anbang.p2p.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.p2p.cqrs.q.dbo.AuthorizationDbo;

public interface AuthorizationDboRepository extends MongoRepository<AuthorizationDbo, String> {

	AuthorizationDbo findOneByThirdAuthAndPublisherAndUuid(boolean thirdAuth, String publisher, String uuid);

	AuthorizationDbo findOneByThirdAuthAndPublisherAndUserId(boolean thirdAuth, String publisher, String userId);

}
