package org.tju.HFDemo.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tju.HFDemo.core.manager.ca.CAManager;
import org.tju.HFDemo.web.manager.TokenManager;
import org.tju.HFDemo.web.model.User;
import org.tju.HFDemo.web.service.AbstractService;
import org.tju.HFDemo.web.service.AuthenticationService;
import org.tju.HFDemo.web.service.UserService;

/**
 * Created by shaohan.yin on 05/05/2017.
 */
@Service
public class DefaultUserService extends AbstractService implements UserService {
    @Autowired
    private CAManager caManager;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private AuthenticationService authenticationService;


    @Override
    public User login(User user) {
        org.tju.HFDemo.core.role.User hfUser = caManager.enroll(user.getUserId(), user.getUserSecret());
        if(null != hfUser) {
            user.setToken(authenticationService.generateToken(user));
            tokenManager.putToken(user.getUserId(), user.getToken(), hfUser);
            user.setUserSecret("");
        }
        return user;
    }

    @Override
    public void logout(User user) {
        tokenManager.deleteToken(user.getUserId(), user.getToken());
    }

}
