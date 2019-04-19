package com.anbang.p2p.cqrs.q.service;

import com.anbang.p2p.cqrs.q.dao.RefundInfoDao;
import com.anbang.p2p.cqrs.q.dbo.RefundInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 */
@Service
public class RefundInfoService {
    @Autowired
    private RefundInfoDao refundInfoDao;

    public void save(RefundInfo info){
        refundInfoDao.save(info);
    }

    public RefundInfo getById(String id){
        return refundInfoDao.getById(id);
    }

    public long getAmountByUserId(String userId){
        return refundInfoDao.getAmountByUserId(userId);
    }

    public List<RefundInfo> findByUserId(int page, int size, String userId){
        return refundInfoDao.findByUserId(page,size,userId);
    }

    public void updataStatus(String id, String status){
        refundInfoDao.updateStatus(id, status);
    }

}
