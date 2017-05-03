package org.tju.HFDemo.core.role;

import org.hyperledger.fabric.sdk.Enrollment;

import java.util.Set;

/**
 * Created by shaohan.yin on 02/05/2017.
 */
public class DefaultUser implements User {
    private String name;
    private Set<String> roles;
    private String account;
    private String affiliation;
    private Enrollment enrollment;
    private String mspid;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getAffiliation() {
        return affiliation;
    }

    @Override
    public Enrollment getEnrollment() {
        return enrollment;
    }

    @Override
    public String getMSPID() {
        return mspid;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    @Override
    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    @Override
    public void setMSPID(String mspid) {
        this.mspid = mspid;
    }
}
