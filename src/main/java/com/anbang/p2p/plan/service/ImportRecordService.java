package com.anbang.p2p.plan.service;

import com.anbang.p2p.plan.bean.ImportRecord;
import com.anbang.p2p.plan.dao.ImportRecordDao;
import com.highto.framework.web.page.ListPage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportRecordService {
    @Autowired
    private ImportRecordDao importRecordDao;

    public void save(ImportRecord order){
        importRecordDao.save(order);
    }

    public void delelte(String id){
        importRecordDao.delelte(id);
    }

    public ImportRecord getById(String id){
        return importRecordDao.getById(id);
    }

    public long getAmount(ImportRecord importRecord){
        return importRecordDao.getAmount(importRecord);
    }

    public ListPage findByBean(int page, int size, ImportRecord importRecord){
        long count = importRecordDao.getAmount(importRecord);
        List<ImportRecord> records = importRecordDao.findByBean(page, size, importRecord);
        if (CollectionUtils.isNotEmpty(records)) {
            records.forEach((record) -> record.setRepayRecords(null));
        }
        return new ListPage(records, page, size, (int)count);
    }
}
