package com.anbang.p2p.cqrs.q.dao;

import java.util.List;

import com.anbang.p2p.cqrs.q.dbo.UserBankCardInfo;

public interface UserBankCardInfoDao {

	void save(UserBankCardInfo info);

	long getAmountByUserId(String userId);

	List<UserBankCardInfo> findByUserId(int page, int size, String userId);

	UserBankCardInfo findById(String cardId);
}
