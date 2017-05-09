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
        try {
            return hfManager.getStudentInfo(tokenManager.getHFUserFromToken(user.getUserId(), user.getToken()),
                    user.getUserId());
        } catch (ExecutionException e) {
            logger.error("[DefaultStudentInfoService][getInfo][fail]{}", user.getUserId(), e);
            throw new HFDRuntimeException(e.getMessage());
        }
    }

    @Override
    public void insertInfo(User user, StudentInfo info) {
        updateInfo(user, info);
    }

    @Override
    public void updateInfo(User user, StudentInfo info) {
        try {
            hfManager.updateStudentInfo(tokenManager.getHFUserFromToken(user.getUserId(), user.getToken()),
                    info);
        } catch (ExecutionException e) {
            logger.error("[DefaultStudentInfoService][update][fail]{}", user.getUserId(), e);
            throw new HFDRuntimeException(e.getMessage());
        }
    }

    @Override
    public void removeInfo(User user) {
        try {
            hfManager.removeStudentInfo(tokenManager.getHFUserFromToken(user.getUserId(), user.getToken()),
                    user.getUserId());
        } catch (ExecutionException e) {
            logger.error("[DefaultStudentInfoService][remove][fail]{}", user.getUserId(), e);
            throw new HFDRuntimeException(e.getMessage());
        }
    }
}
