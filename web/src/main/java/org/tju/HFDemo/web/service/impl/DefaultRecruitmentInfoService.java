package org.tju.HFDemo.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tju.HFDemo.core.dto.RecruitmentInfo;
import org.tju.HFDemo.core.manager.hf.HFManager;
import org.tju.HFDemo.web.service.AbstractService;
import org.tju.HFDemo.web.service.RecruitmentInfoService;

import java.util.List;

/**
 * Created by shaohan.yin on 12/05/2017.
 */
@Service
public class DefaultRecruitmentInfoService extends AbstractService implements RecruitmentInfoService {
    @Autowired
    private HFManager hfManager;

    @Override
    public List<RecruitmentInfo> getRecruitmentInfos() {
        return hfManager.getRecruitmentInfos();
    }
}
