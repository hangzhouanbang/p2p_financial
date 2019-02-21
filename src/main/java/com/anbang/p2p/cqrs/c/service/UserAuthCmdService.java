package com.anbang.p2p.cqrs.c.service;

import com.anbang.p2p.cqrs.c.domain.user.CreateUserResult;
import com.dml.users.AuthorizationAlreadyExistsException;

public interface UserAuthCmdService {

	CreateUserResult createUserAndAddThirdAuth(String publisher, String uuid, Long currentTime)
			throws AuthorizationAlreadyExistsException;
}
