package com.anbang.p2p.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.c.domain.user.CreateUserResult;
import com.anbang.p2p.cqrs.c.domain.user.UserIdManager;
import com.anbang.p2p.cqrs.c.service.UserAuthCmdService;
import com.dml.users.AuthorizationAlreadyExistsException;
import com.dml.users.ThirdAuthorization;
import com.dml.users.UsersManager;

@Component
public class UserAuthCmdServiceImpl extends CmdServiceBase implements UserAuthCmdService {

	@Override
	public CreateUserResult createUserAndAddThirdAuth(String publisher, String uuid, Long currentTime)
			throws AuthorizationAlreadyExistsException {
		UserIdManager userIdManager = singletonEntityRepository.getEntity(UserIdManager.class);
		UsersManager usersManager = singletonEntityRepository.getEntity(UsersManager.class);
		String userId = userIdManager.createUserId(currentTime);
		ThirdAuthorization auth = new ThirdAuthorization();
		auth.setPublisher(publisher);
		auth.setUuid(uuid);
		try {
			usersManager.createUserWithAuth(userId, auth);
			return new CreateUserResult(userId, publisher, uuid);
		} catch (AuthorizationAlreadyExistsException e) {
			userIdManager.removeUserId(userId);
			throw e;
		}
	}

}
