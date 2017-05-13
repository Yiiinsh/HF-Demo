package org.tju.HFDemo.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tju.HFDemo.core.manager.hf.HFManager;
import org.tju.HFDemo.web.model.BlockInfo;
import org.tju.HFDemo.web.service.AbstractService;
import org.tju.HFDemo.web.service.BlockInfoService;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by shaohan.yin on 13/05/2017.
 */
@Service
public class DefaultBlockInfoService extends AbstractService implements BlockInfoService {
    @Autowired
    private HFManager hfManager;

    @Override
    public List<BlockInfo> getBlockInfos() {
        List<BlockInfo> res = new LinkedList<>();

        hfManager.getBlocks().stream().forEach(blockInfo -> {
            BlockInfo info = new BlockInfo();
            info.setNumber(blockInfo.getBlockNumber());
            info.setPreviousHash(blockInfo.getPreviousHash().toString());
            info.setData(blockInfo.getBlock().getDataOrBuilder().getData(0).toStringUtf8());

            res.add(info);
        });

        return res;
    }
}
