package com.anbang.p2p.plan.dao.mongodb;

import com.anbang.p2p.plan.bean.RiskInfo;
import com.anbang.p2p.plan.dao.RiskInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongodbRiskInfoDao implements RiskInfoDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(RiskInfo riskInfo) {
        mongoTemplate.save(riskInfo);
    }

    @Override
    public RiskInfo getById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, RiskInfo.class);
    }

    @Override
    public RiskInfo getByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        return mongoTemplate.findOne(query, RiskInfo.class);
    }
}
