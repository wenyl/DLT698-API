package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.entity.Frame;
import cn.com.wenyl.bs.dlt698.service.BaseFrameParser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@SuppressWarnings("unchecked")
public class FrameParseProcessor {
    @Resource
    private FrameParserFactory factory;
    // 自动获取当前类的 Frame 类型对应的 Builder
    // 通过泛型自动获取解析器并解析帧数据
    public <T extends Frame> Frame parseFrame(Class frameType, byte[] frameBytes) throws RuntimeException{
        // 使用解析器解析帧数据
        return factory.getFrameParser(frameType).parseFrame(frameBytes);
    }

    public BaseFrameParser getFrameParser(Class frameType) {
        return factory.getFrameParser(frameType);
    }
}
