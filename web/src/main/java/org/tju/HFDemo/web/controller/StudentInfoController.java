package org.tju.HFDemo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tju.HFDemo.core.dto.StudentInfo;
import org.tju.HFDemo.web.model.StudentInfoRequest;
import org.tju.HFDemo.web.model.User;
import org.tju.HFDemo.web.service.StudentInfoService;

/**
 * Created by shaohan.yin on 09/05/2017.
 */
@RestController
@RequestMapping("/info/student")
public class StudentInfoController extends AbstractController {
    @Autowired
    private StudentInfoService studentInfoService;

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public StudentInfo getStudentInfo(@RequestBody User user) {
        return studentInfoService.getInfo(user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateStudentInfo(@RequestBody StudentInfoRequest request) {
        User user = request.getUser();
        StudentInfo info = request.getInfo();
        studentInfoService.updateInfo(user, info);
    }

}
