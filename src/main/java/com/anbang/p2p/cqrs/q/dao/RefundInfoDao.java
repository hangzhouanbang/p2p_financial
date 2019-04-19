package com.anbang.p2p.cqrs.q.dao;

import java.util.List;

import com.anbang.p2p.cqrs.q.dbo.RefundInfo;

public interface RefundInfoDao {

	void save(RefundInfo info);

	long getAmountByUserId(String userId);

	RefundInfo getById(String id);

	List<RefundInfo> findByUserId(int page, int size, String userId);

	void updateStatus(String id, String status);
}
