package com.anbang.p2p.plan.dao.mongodb;

import com.anbang.p2p.plan.bean.ImportRecord;
import com.anbang.p2p.plan.dao.ImportRecordDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongodbImportRecordDao implements ImportRecordDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(ImportRecord order) {
        mongoTemplate.save(order);
    }

    @Override
    public void delelte(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, ImportRecord.class);
    }

    @Override
    public ImportRecord getById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, ImportRecord.class);
    }

    @Override
    public long getAmount(ImportRecord record) {
        Query query = new Query();
        if (StringUtils.isNotBlank(record.getName())) {
            query.addCriteria(Criteria.where("name").is(record.getName()));
        }

        return mongoTemplate.count(query, ImportRecord.class);
    }

    @Override
    public List<ImportRecord> findByBean(int page, int size, ImportRecord record) {
        Query query = new Query();
        if (StringUtils.isNotBlank(record.getName())) {
            query.addCriteria(Criteria.where("name").is(record.getName()));
        }

        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        query.skip((page - 1) * size);
        query.limit(size);
        return mongoTemplate.find(query, ImportRecord.class);
    }
}
