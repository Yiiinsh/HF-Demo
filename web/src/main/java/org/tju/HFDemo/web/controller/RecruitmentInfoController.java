package org.tju.HFDemo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tju.HFDemo.core.dto.RecruitmentInfo;
import org.tju.HFDemo.web.service.RecruitmentInfoService;

import java.util.List;

/**
 * Created by shaohan.yin on 12/05/2017.
 */
@RestController
@RequestMapping("/info/recruitment")
public class RecruitmentInfoController extends AbstractController {
    @Autowired
    private RecruitmentInfoService recruitmentInfoService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public List<RecruitmentInfo> getRecruitmentInfo() {
        return recruitmentInfoService.getRecruitmentInfos();
    }
}
