package org.tju.HFDemo.web.service;

import org.tju.HFDemo.web.model.User;

/**
 * Created by shaohan.yin on 05/05/2017.
 */
public interface UserService {
    User login(User user);
    void logout(User user);
}
