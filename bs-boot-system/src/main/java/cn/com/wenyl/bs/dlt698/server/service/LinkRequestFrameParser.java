package cn.com.wenyl.bs.dlt698.server.service;

import cn.com.wenyl.bs.dlt698.common.constants.DataType;
import cn.com.wenyl.bs.dlt698.common.service.BaseFrameParser;
import cn.com.wenyl.bs.dlt698.server.entity.LinkRequestData;
import cn.com.wenyl.bs.dlt698.server.entity.LinkRequestFrame;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.utils.ASN1DecoderUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("linkResponseFrameParser")
public class LinkRequestFrameParser implements BaseFrameParser<LinkRequestFrame, LinkRequestData> {
    @Override
    public LinkRequestFrame parseFrame(byte[] frameBytes) throws RuntimeException{
        LinkRequestFrame frame  = new LinkRequestFrame();
        if(!FrameParseUtils.checkFrame(frameBytes)){
            String errorInfo = "无效帧：起始符或结束符错误,当前帧起始符--"+HexUtils.byteToHex(frameBytes[0])+",结束符--"+HexUtils.byteToHex(frameBytes[frameBytes.length-1]);
            log.error(errorInfo);
            throw new RuntimeException(errorInfo);
        }
        FrameDto frameDto = FrameParseUtils.getFrameDto(frameBytes);
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
        linkRequestData.setApdu(userDataBytes[0]);
        linkRequestData.setPIID(userDataBytes[1]);
        byte[] heartbeatInterval = new byte[2];
        System.arraycopy(userDataBytes,2,heartbeatInterval,0,2);
        linkRequestData.setHeartbeatIntervalBytes(heartbeatInterval);
        Object object = FrameParseUtils.getData(DataType.LONG_UNSIGNED, heartbeatInterval);
        if(object != null){
            linkRequestData.setHeartbeatInterval((int)object);
        }
        byte[] dateTime = new byte[10];
        System.arraycopy(userDataBytes,5,dateTime,0,10);
        linkRequestData.setRequestTimeBytes(dateTime);
        linkRequestData.setRequestTime(ASN1DecoderUtils.decodeDateTime(dateTime));
        return linkRequestData;
    }

    @Override
    public Object getData(LinkRequestFrame frame) throws RuntimeException {
        return null;
    }
}
