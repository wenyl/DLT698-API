package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.entity.LinkUserData;
import cn.com.wenyl.bs.dlt698.common.service.BaseFrameBuilder;
import cn.com.wenyl.bs.dlt698.common.entity.Frame;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FrameBuildProcessor {
    @Resource
    private FrameBuilderFactory factory;
    // 自动获取当前类的 Frame 类型对应的 Builder
    public <T extends Frame,G extends LinkUserData> BaseFrameBuilder<T> getFrameBuilder(Class<T> frameType) {

        return factory.getFrameBuilder(frameType);
    }

}
