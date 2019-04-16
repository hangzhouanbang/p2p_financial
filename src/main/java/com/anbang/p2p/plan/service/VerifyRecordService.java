package com.anbang.p2p.plan.service;

import com.anbang.p2p.plan.bean.VerifyRecord;
import com.anbang.p2p.plan.dao.VerifyRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyRecordService {
    @Autowired
    private VerifyRecordDao verifyRecordDao;

    public void save(VerifyRecord verifyRecord){
        verifyRecordDao.save(verifyRecord);
    }
    public VerifyRecord getById(String id){
        return verifyRecordDao.getById(id);
    }

    public VerifyRecord getByUerId(String uerId){
        return verifyRecordDao.getByUerId(uerId);
    }

    public void updateStateAndCause(String id, String state, String result, String causeBy){
        verifyRecordDao.updateStateAndCause(id, state, result ,causeBy);
    }
}
