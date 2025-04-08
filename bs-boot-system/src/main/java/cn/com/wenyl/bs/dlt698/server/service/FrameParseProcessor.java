package cn.com.wenyl.bs.dlt698.server.service;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.BaseFrameParser;
import cn.com.wenyl.bs.dlt698.client.service.impl.FrameParserFactory;
import cn.com.wenyl.bs.dlt698.common.constants.ClientAPDU;
import cn.com.wenyl.bs.dlt698.common.entity.Frame;
import cn.com.wenyl.bs.dlt698.common.entity.LinkUserData;
import cn.com.wenyl.bs.dlt698.server.entity.LinkRequestData;
import cn.com.wenyl.bs.dlt698.server.entity.LinkRequestFrame;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
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
