package com.anbang.p2p.cqrs.q.dao.mongodb;

import com.anbang.p2p.cqrs.q.dao.AgentPayRecordDao;
import com.anbang.p2p.cqrs.q.dbo.AgentPayRecord;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 */
@Component
public class MongoAgentPayRecordDao implements AgentPayRecordDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(AgentPayRecord agentPayRecord) {
        mongoTemplate.save(agentPayRecord);
    }

    @Override
    public long getAmountByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.count(query, AgentPayRecord.class);
    }

    @Override
    public List<AgentPayRecord> findByUserId(int page, int size, String userId) {
        Query query = new Query();
        if (StringUtil.isBlank(userId)) {
            query.addCriteria(Criteria.where("userId").is(userId));
        }
        query.skip((page - 1) * size);
        query.limit(size);
        return mongoTemplate.find(query, AgentPayRecord.class);
    }

    @Override
    public void updataStatus(String id, String status) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("status", status);
        mongoTemplate.updateFirst(query, update, AgentPayRecord.class);
    }


}
