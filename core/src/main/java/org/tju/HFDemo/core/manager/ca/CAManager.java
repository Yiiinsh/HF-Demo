package org.tju.HFDemo.core.manager.ca;

import org.tju.HFDemo.core.role.User;

/**
 * Created by shaohan.yin on 30/04/2017.
 */
public interface CAManager {
    User enroll(String userName, String passwd);
    void register(String userName, String passwd, User registar);
}
