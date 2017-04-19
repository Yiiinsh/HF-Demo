package org.tju.HFDemo.core.role;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by shaohan.yin on 19/04/2017.
 */
public class TestUser implements User, Serializable {

    private String name;
    private Set<String> roles;
    private String account;
    private String affiliation;
    private Enrollment enrollment;
    private String mspid;

    public TestUser(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    @Override
    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    @Override
    public String getMSPID() {
        return mspid;
    }

    public void setMSPID(String mspid) {
        this.mspid = mspid;
    }
}
