package org.tju.HFDemo.web.service;

import org.tju.HFDemo.core.dto.StudentInfo;
import org.tju.HFDemo.web.model.User;

/**
 * Created by shaohan.yin on 09/05/2017.
 */
public interface StudentInfoService {
    StudentInfo getInfo(User user);
    void insertInfo(User user, StudentInfo info);
    void updateInfo(User user, StudentInfo info);
    void removeInfo(User user);
}
