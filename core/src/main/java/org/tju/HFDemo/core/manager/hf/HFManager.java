package org.tju.HFDemo.core.manager.hf;

import org.hyperledger.fabric.sdk.Chain;
import org.tju.HFDemo.core.dto.StudentInfo;
import org.tju.HFDemo.core.role.User;

import java.util.List;

/**
 * Created by shaohan.yin on 30/04/2017.
 */
public interface HFManager {
    Chain getChain(String name);
    StudentInfo getStudentInfo(User user, String id);
    List<StudentInfo> getStudentInfoBatch(User user, String startId, String endId);
    void updateStudentInfo(User user, StudentInfo studentInfo);
    void removeStudentInfo(User user, StudentInfo studentInfo);
}
