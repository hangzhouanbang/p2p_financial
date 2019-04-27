package com.anbang.p2p.plan.dao.mongodb;

import com.anbang.p2p.plan.bean.LoanStateRecord;
import com.anbang.p2p.plan.dao.LoanStateRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongodbLoanStateRecordDao implements LoanStateRecordDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(LoanStateRecord loanStateRecord) {
        mongoTemplate.save(loanStateRecord);
    }

    @Override
    public List<LoanStateRecord> list(String orderId) {
        Query query = new Query(Criteria.where("orderId").is(orderId));
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        return mongoTemplate.find(query, LoanStateRecord.class);
    }
}
