package org.tju.HFDemo.core.manager.hf;

import org.hyperledger.fabric.sdk.Chain;
import org.tju.HFDemo.core.dto.StudentInfo;
import org.tju.HFDemo.core.manager.AbstractManager;
import org.tju.HFDemo.core.role.User;

import java.util.List;
import java.util.Map;

/**
 * Created by shaohan.yin on 02/05/2017.
 */
public class DefaultHFManager extends AbstractManager implements HFManager {
    private Map<String, Chain> chains;


    @Override
    public Chain getChain(String name) {
        return null;
    }

    @Override
    public StudentInfo getStudentInfo(User user, String id) {
        return null;
    }

    @Override
    public List<StudentInfo> getStudentInfoBatch(User user, String startId, String endId) {
        return null;
    }

    @Override
    public void updateStudentInfo(User user, StudentInfo studentInfo) {

    }

    @Override
    public void removeStudentInfo(User user, StudentInfo studentInfo) {

    }
}
