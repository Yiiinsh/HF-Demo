package org.tju.HFDemo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tju.HFDemo.web.model.BlockInfo;
import org.tju.HFDemo.web.service.BlockInfoService;

import java.util.List;

/**
 * Created by shaohan.yin on 12/05/2017.
 */
@RestController
@RequestMapping("/info/block")
public class BlockInfoController extends AbstractController {
    @Autowired
    private BlockInfoService blockInfoService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<BlockInfo> getBlockInfos() {
        return blockInfoService.getBlockInfos();
    }

}
