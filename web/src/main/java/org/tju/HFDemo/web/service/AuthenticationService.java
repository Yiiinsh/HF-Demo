package org.tju.HFDemo.web.service;

import org.tju.HFDemo.web.model.User;

/**
 * Created by shaohan.yin on 06/05/2017.
 */
public interface AuthenticationService {
    String generateToken(User user);
}
