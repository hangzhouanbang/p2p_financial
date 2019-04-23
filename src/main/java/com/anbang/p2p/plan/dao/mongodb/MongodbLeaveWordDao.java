package com.anbang.p2p.plan.dao.mongodb;

import com.anbang.p2p.plan.bean.LeaveWord;
import com.anbang.p2p.plan.dao.LeaveWordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongodbLeaveWordDao implements LeaveWordDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(LeaveWord leaveWord) {
        mongoTemplate.save(leaveWord);
    }

    @Override
    public void deleteByIds(String[] ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        mongoTemplate.remove(query, LeaveWord.class);
    }

    @Override
    public long getCount() {
        return mongoTemplate.count(new Query(), LeaveWord.class);
    }

    @Override
    public List<LeaveWord> list(int page, int size) {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        query.skip((page - 1) * size);
        query.limit(size);
        return mongoTemplate.find(query, LeaveWord.class);
    }
}
