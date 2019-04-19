package com.anbang.p2p.cqrs.q.service;

import com.anbang.p2p.cqrs.q.dao.AgentPayRecordDao;
import com.anbang.p2p.cqrs.q.dbo.AgentPayRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 */
@Service
public class AgentPayRecordService {
    @Autowired
    private AgentPayRecordDao agentPayRecordDao;

    public void save(AgentPayRecord info){
        agentPayRecordDao.save(info);
    }

    public long getAmountByUserId(String userId){
        return agentPayRecordDao.getAmountByUserId(userId);
    }

    public List<AgentPayRecord> findByUserId(int page, int size, String userId){
        return agentPayRecordDao.findByUserId(page,size,userId);
    }

    public void updataStatus(String id, String status){
        agentPayRecordDao.updataStatus(id, status);
    }
}
