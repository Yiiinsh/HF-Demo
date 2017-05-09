package org.tju.HFDemo.web.model;

import org.tju.HFDemo.core.dto.StudentInfo;

/**
 * Created by shaohan.yin on 09/05/2017.
 */
public class StudentInfoRequest {
    private User user;
    private StudentInfo info;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StudentInfo getInfo() {
        return info;
    }

    public void setInfo(StudentInfo info) {
        this.info = info;
    }
}
