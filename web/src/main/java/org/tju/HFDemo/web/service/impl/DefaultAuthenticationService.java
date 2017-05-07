package org.tju.HFDemo.web.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;
import org.tju.HFDemo.web.model.User;
import org.tju.HFDemo.web.service.AbstractService;
import org.tju.HFDemo.web.service.AuthenticationService;

import java.io.UnsupportedEncodingException;

/**
 * Created by shaohan.yin on 06/05/2017.
 */
@Service
public class DefaultAuthenticationService extends AbstractService implements AuthenticationService {
    @Override
    public String generateToken(User user) {
        String token = null;
        try {
            token = JWT.create().withAudience(user.getUserId()).sign(Algorithm.HMAC256(user.getUserSecret()));
        } catch (UnsupportedEncodingException e) {
            logger.error("[GenerateToken][fail]User:{}", user.getUserId(), e);
        }
        return token;
    }
}
