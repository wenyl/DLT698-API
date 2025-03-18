package cn.com.wenyl.bs.dlt698.service.impl;


import cn.com.wenyl.bs.dlt698.service.CheckBuildService;
import cn.com.wenyl.bs.dlt698.utils.FrameCheckUtils;
import org.springframework.stereotype.Service;

@Service("checkBuildService")
public class CheckBuildServiceImpl implements CheckBuildService {
    @Override
    public void buildHCS(byte[] headFrame,int length) {
        FrameCheckUtils.tryCS16(headFrame, length);
    }

    @Override
    public void buildFCS(byte[] bodyFrame,int length) {
        FrameCheckUtils.tryCS16(bodyFrame, length);
    }
}
