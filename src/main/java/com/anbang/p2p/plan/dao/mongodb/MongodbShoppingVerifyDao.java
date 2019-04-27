package com.anbang.p2p.plan.dao.mongodb;

import com.anbang.p2p.plan.bean.ShoppingVerify;
import com.anbang.p2p.plan.dao.ShoppingVerifyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MongodbShoppingVerifyDao implements ShoppingVerifyDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(ShoppingVerify shoppingVerify) {
        mongoTemplate.save(shoppingVerify);
    }

    @Override
    public ShoppingVerify getById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, ShoppingVerify.class);
    }

    @Override
    public void updateStateAndData(String id, String state, String report) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("state", state);
        update.set("report", report);
        mongoTemplate.updateFirst(query, update, ShoppingVerify.class);
    }
}
