package com.anbang.p2p.plan.dao.mongodb;

import com.anbang.p2p.plan.bean.MobileVerify;
import com.anbang.p2p.plan.bean.RiskInfo;
import com.anbang.p2p.plan.dao.MobileVerifyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @Description:
 */
@Component
public class MongodbMobileVerifyDao implements MobileVerifyDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(MobileVerify mobileVerify) {
        mongoTemplate.save(mobileVerify);
    }

    @Override
    public MobileVerify getById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, MobileVerify.class);
    }

    @Override
    public void updateStateAndData(String id, String state, String report) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("state", state);
        update.set("report", report);
        mongoTemplate.updateFirst(query, update, MobileVerify.class);
    }
}
