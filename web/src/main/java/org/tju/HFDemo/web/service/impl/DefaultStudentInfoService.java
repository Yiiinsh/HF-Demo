package org.tju.HFDemo.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tju.HFDemo.core.dto.StudentInfo;
import org.tju.HFDemo.core.exception.HFDRuntimeException;
import org.tju.HFDemo.core.manager.hf.HFManager;
import org.tju.HFDemo.web.manager.TokenManager;
import org.tju.HFDemo.web.model.User;
import org.tju.HFDemo.web.service.AbstractService;
import org.tju.HFDemo.web.service.StudentInfoService;

import java.util.concurrent.ExecutionException;

/**
 * Created by shaohan.yin on 09/05/2017.
 */
@Service
public class DefaultStudentInfoService extends AbstractService implements StudentInfoService {
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private HFManager hfManager;

    @Override
    public StudentInfo getInfo(User user) {
        return hfManager.getStudentInfo(getUser(user), user.getUserId());
    }

    @Override
    public void insertInfo(User user, StudentInfo info) {
        updateInfo(user, info);
    }

    @Override
    public void updateInfo(User user, StudentInfo info) {
        hfManager.updateStudentInfo(getUser(user), info);
    }

    @Override
    public void removeInfo(User user) {
        hfManager.removeStudentInfo(getUser(user), user.getUserId());
    }

    private org.tju.HFDemo.core.role.User getUser(User user) {
        if (tokenManager.checkToken(user.getUserId(), user.getToken())) {
            try {
                return tokenManager.getHFUserFromToken(user.getUserId(), user.getToken());
            } catch (ExecutionException e) {
                throw new HFDRuntimeException(e.getMessage());
            }
        }
        throw new HFDRuntimeException("Token expired.Please re-login.");
    }

}
