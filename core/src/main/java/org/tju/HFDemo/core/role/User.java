package org.tju.HFDemo.core.role;

import org.hyperledger.fabric.sdk.Enrollment;

/**
 * Created by shaohan.yin on 24/04/2017.
 */
public interface User extends org.hyperledger.fabric.sdk.User{
    static User newDefaultUser(String userName) {
        User user = new DefaultUser();
        user.setName(userName);
        return user;
    }

    void setName(String name);
    void setAffiliation(String affiliation);
    void setEnrollment(Enrollment enrollment);
    void setMSPID(String mspid);
}
