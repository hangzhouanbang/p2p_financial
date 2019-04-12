package com.anbang.p2p.plan.dao.mongodb;

import com.anbang.p2p.plan.bean.VerifyRecord;
import com.anbang.p2p.plan.dao.VerifyRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongodbVerifyRecordDao implements VerifyRecordDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(VerifyRecord verifyRecord) {
        mongoTemplate.save(verifyRecord);
    }

    @Override
    public VerifyRecord getById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, VerifyRecord.class);
    }

    @Override
    public VerifyRecord getByUerId(String uerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("uerId").is(uerId));
        return mongoTemplate.findOne(query, VerifyRecord.class);
    }
}
