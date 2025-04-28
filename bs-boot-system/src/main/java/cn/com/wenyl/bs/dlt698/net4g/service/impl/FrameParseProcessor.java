package cn.com.wenyl.bs.dlt698.net4g.service.impl;
import cn.com.wenyl.bs.dlt698.common.service.BaseFrameParser;
import cn.com.wenyl.bs.dlt698.common.service.impl.FrameParserFactory;
import cn.com.wenyl.bs.dlt698.common.entity.Frame;
import cn.com.wenyl.bs.dlt698.common.entity.LinkUserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Slf4j
@Service
public class FrameParseProcessor {
    @Resource
    private FrameParserFactory factory;


    public <T extends Frame, G extends LinkUserData> BaseFrameParser<T, G> getFrameParser(Class<T> frameType,Class<G> linkUserDataType) {
        return factory.getParser(frameType,linkUserDataType);
    }
}
