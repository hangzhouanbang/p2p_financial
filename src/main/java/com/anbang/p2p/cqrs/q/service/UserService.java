package com.anbang.p2p.cqrs.q.service;

import com.anbang.p2p.cqrs.c.domain.order.OrderState;
import com.anbang.p2p.cqrs.q.dao.UserDboDao;
import com.anbang.p2p.cqrs.q.dbo.UserDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDboDao userDboDao;

    public void getAndUpdateOverdueCount(String userId) {
        UserDbo userDbo = userDboDao.findById(userId);
        if (userDbo != null) {
            userDboDao.updateCountAndState(userId, null, userDbo.getOverdueCount() + 1, OrderState.overdue.name());
        }
    }

    public UserDbo getById(String userId) {
        return userDboDao.findById(userId);
    }

    public void updateUserState(String userId, String state) {
        userDboDao.updateUserState(userId, state);
    }
}
