package com.anbang.p2p.plan.dao.mongodb;

import com.anbang.p2p.plan.bean.OrgInfo;
import com.anbang.p2p.plan.dao.OrgInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongodbOrgInfoDao implements OrgInfoDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(OrgInfo orgInfo) {
        mongoTemplate.save(orgInfo);
    }

    @Override
    public OrgInfo getById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, OrgInfo.class);
    }
}
