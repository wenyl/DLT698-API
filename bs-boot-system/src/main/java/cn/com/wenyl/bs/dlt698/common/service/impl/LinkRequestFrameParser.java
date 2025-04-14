package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.DataType;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.BaseFrameParser;
import cn.com.wenyl.bs.dlt698.net4g.entity.LinkRequestData;
import cn.com.wenyl.bs.dlt698.net4g.entity.LinkRequestFrame;
import cn.com.wenyl.bs.dlt698.utils.ASN1DecoderUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("linkRequestFrameParser")
public class LinkRequestFrameParser implements BaseFrameParser<LinkRequestFrame, LinkRequestData> {
    public LinkRequestFrame parseFrame(FrameDto frameDto) throws RuntimeException{
        LinkRequestFrame frame  = new LinkRequestFrame();
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
