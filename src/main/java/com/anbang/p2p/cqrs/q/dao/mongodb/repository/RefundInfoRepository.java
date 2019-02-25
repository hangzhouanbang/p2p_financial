package com.anbang.p2p.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.p2p.cqrs.q.dbo.RefundInfo;

public interface RefundInfoRepository extends MongoRepository<RefundInfo, String> {

}
