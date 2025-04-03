package cn.com.wenyl.bs.dlt698.server.service;

import cn.com.wenyl.bs.dlt698.client.constants.DataType;
import cn.com.wenyl.bs.dlt698.client.constants.GetResponse;
import cn.com.wenyl.bs.dlt698.client.constants.GetResultType;
import cn.com.wenyl.bs.dlt698.client.constants.ServerAPDU;
import cn.com.wenyl.bs.dlt698.client.entity.GetResponseNormalData;
import cn.com.wenyl.bs.dlt698.server.entity.LinkRequestData;
import cn.com.wenyl.bs.dlt698.server.entity.LinkRequestFrame;
import cn.com.wenyl.bs.dlt698.common.OAD;
import cn.com.wenyl.bs.dlt698.client.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.client.service.BaseFrameParser;
import cn.com.wenyl.bs.dlt698.common.BaseFrameParserImpl;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("linkResponseFrameParser")
public class LinkRequestFrameParser extends BaseFrameParserImpl<LinkRequestFrame, LinkRequestData> implements BaseFrameParser<LinkRequestFrame, LinkRequestData> {
    @Override
    public LinkRequestFrame parseFrame(byte[] frameBytes) throws RuntimeException{
        LinkRequestFrame frame  = new LinkRequestFrame();
        if(!super.checkFrame(frameBytes)){
            String errorInfo = "无效帧：起始符或结束符错误,当前帧起始符--"+HexUtils.byteToHex(frameBytes[0])+",结束符--"+HexUtils.byteToHex(frameBytes[frameBytes.length-1]);
            log.error(errorInfo);
            throw new RuntimeException(errorInfo);
        }
        FrameDto frameDto = super.getFrameDto(frameBytes);
        LinkRequestData linkRequestData = this.parseLinkUserData(frameDto.getUserData());
        frame.setLengthDomain(frameDto.getLengthDomain());
        frame.setControlDomain(frameDto.getControlDomain());
        frame.setAddressDomain(frameDto.getAddressDomain());
        frame.setHcs(frameDto.getHcs());
        frame.setLinkRequestData(linkRequestData);
        frame.setFcs(frameDto.getFcs());
        return frame;
    }

    @Override
    public LinkRequestData parseLinkUserData(byte[] userDataBytes){
        LinkRequestData linkRequestData = new LinkRequestData();
        log.info("用户完整数据{}", HexUtils.bytesToHex(userDataBytes));
        return linkRequestData;
    }

    @Override
    public Object getData(LinkRequestFrame frame) throws RuntimeException {
        return null;
    }
}
