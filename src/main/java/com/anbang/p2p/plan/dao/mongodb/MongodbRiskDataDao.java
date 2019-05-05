package com.anbang.p2p.plan.dao.mongodb;

import com.anbang.p2p.plan.bean.RiskData;
import com.anbang.p2p.plan.dao.RiskDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongodbRiskDataDao implements RiskDataDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(RiskData riskData) {
        mongoTemplate.save(riskData);
    }

    @Override
    public RiskData getById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, RiskData.class);
    }
}
