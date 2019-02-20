package com.anbang.p2p.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.plan.bean.BaseLoan;
import com.anbang.p2p.plan.dao.BaseLoanDao;
import com.anbang.p2p.plan.dao.mongodb.repository.BaseLoanRepository;

@Component
public class MongodbBaseLoanDao implements BaseLoanDao {

	@Autowired
	private BaseLoanRepository baseLoanRepository;

	@Override
	public void save(BaseLoan loan) {
		baseLoanRepository.save(loan);
	}

}
