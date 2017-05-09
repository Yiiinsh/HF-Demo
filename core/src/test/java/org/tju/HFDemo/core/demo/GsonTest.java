package org.tju.HFDemo.core.demo;

import com.google.gson.Gson;
import org.junit.Test;
import org.tju.HFDemo.core.AbstractTest;
import org.tju.HFDemo.core.dto.InternInfo;
import org.tju.HFDemo.core.dto.StudentInfo;

/**
 * Created by shaohan.yin on 08/05/2017.
 */
public class GsonTest extends AbstractTest {
    private Gson gson = new Gson();

    @Test
    public void gsonTest() {
        StudentInfo stu = new StudentInfo();
        stu.setId("123456");
        stu.setName("aaa");
        stu.setUniversity("tju");
        stu.setDegree("bachelor");
        stu.setStart("2015-01-01");
        stu.setEnd("2017-01-01");
        stu.addEducationQualifications("school");
        stu.addEducationQualifications("junior high school");
        stu.addEducationQualifications("high school");

        InternInfo internA = new InternInfo();
        internA.setName("aaa");
        internA.setStudentId("123456");
        internA.setWorkingId("S99999");
        internA.setCompany("companyA");
        internA.setDepartment("test");
        internA.setPosition("developer");
        internA.setStart("2016-01-01");
        internA.setEnd("2016-03-10");

        InternInfo internB = new InternInfo();
        internB.setName("aaa");
        internB.setStudentId("123456");
        internB.setWorkingId("B00000");
        internB.setCompany("companyB");
        internB.setDepartment("development");
        internB.setPosition("developer");
        internB.setStart("2016-03-20");
        internB.setEnd("2016-06-10");

        stu.addInternInfos(internA);
        stu.addInternInfos(internB);

        logger.info("{}", gson.toJson(stu));
    }
}
