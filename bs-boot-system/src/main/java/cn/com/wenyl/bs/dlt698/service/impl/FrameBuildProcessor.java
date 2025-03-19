package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.entity.Frame;
import cn.com.wenyl.bs.dlt698.service.BaseFrameBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FrameBuildProcessor {
    @Resource
    private FrameBuilderFactory factory;
    // 自动获取当前类的 Frame 类型对应的 Builder
    public <T extends Frame> BaseFrameBuilder<T> getFrameBuilder(Class<T> frameType) {

        return factory.getFrameBuilder(frameType);
    }

}
