package com.anbang.p2p.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.p2p.cqrs.c.domain.user.CreateUserResult;
import com.anbang.p2p.cqrs.c.service.UserAuthCmdService;
import com.anbang.p2p.cqrs.c.service.impl.UserAuthCmdServiceImpl;
import com.dml.users.AuthorizationAlreadyExistsException;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "UserAuthCmdService")
public class DisruptorUserAuthCmdService extends DisruptorCmdServiceBase implements UserAuthCmdService {

	@Autowired
	private UserAuthCmdServiceImpl userAuthCmdServiceImpl;

	@Override
	public CreateUserResult createUserAndAddThirdAuth(String publisher, String uuid, Long currentTime)
			throws AuthorizationAlreadyExistsException {
		CommonCommand cmd = new CommonCommand(UserAuthCmdServiceImpl.class.getName(), "createUserAndAddThirdAuth",
				publisher, uuid, currentTime);

		DeferredResult<CreateUserResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			CreateUserResult createUserResult = userAuthCmdServiceImpl.createUserAndAddThirdAuth(cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter());
			return createUserResult;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof AuthorizationAlreadyExistsException) {
				throw (AuthorizationAlreadyExistsException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

}
