package com.anbang.p2p.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.highto.framework.ddd.SingletonEntityRepository;

@Component
public class CoreSnapshotFactory {

	@Autowired
	private SingletonEntityRepository singletonEntityRepository;

	// @Autowired
	// private MemberDiamondAccountRepository memberDiamondAccountRepository;

	public CoreSnapshot createSnapshoot() {

		CoreSnapshot memberSnapshot = new CoreSnapshot();
		memberSnapshot.setCreateTime(System.currentTimeMillis());
		memberSnapshot.getContentMap().put(SingletonEntityRepository.class, singletonEntityRepository);

		// memberSnapshot.getContentMap().put(MemberDiamondAccountRepository.class,
		// memberDiamondAccountRepository);
		return memberSnapshot;
	}

}
