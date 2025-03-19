package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.entity.*;
import cn.com.wenyl.bs.dlt698.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.service.BaseFrameParser;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("getResponseNormalFrameParser")
public class GetResponseNormalFrameParser extends BaseFrameParserImpl<GetResponseNormalFrame> implements BaseFrameParser<GetResponseNormalFrame> {
    @Override
    public GetResponseNormalFrame parseFrame(byte[] frameBytes) throws RuntimeException{
        GetResponseNormalFrame frame  = new GetResponseNormalFrame();
        if(!super.checkFrame(frameBytes)){
            String errorInfo = "无效帧：起始符或结束符错误,当前帧起始符--"+HexUtils.byteToHex(frameBytes[0])+",结束符--"+HexUtils.byteToHex(frameBytes[frameBytes.length-1]);
            log.error(errorInfo);
            throw new RuntimeException(errorInfo);
        }
        FrameDto frameDto = super.parseFrameHead(frameBytes);

        byte[] hcs = super.getHCS(frameBytes,frameDto.getOffset());
        frameDto.setHcs(hcs);
        frameDto.setOffset(frameDto.getOffset()+hcs.length);
        if(!super.checkFrameHCS(frameDto)){
            throw new RuntimeException("hcs校验失败"+HexUtils.bytesToHex(hcs));
        }

        byte[] userData = getUserData(frameBytes,frameDto.getOffset());
        frameDto.setUserData(userData);
        frameDto.setOffset(frameDto.getOffset()+userData.length);

        byte[] fcs = super.getFCS(frameBytes,frameDto.getOffset());
        frameDto.setFcs(fcs);
        if(!super.checkFrameFCS(frameDto)){
            throw new RuntimeException("fcs校验失败"+HexUtils.bytesToHex(fcs));
        }
        GetResponseNormalData getResponseNormalData = parseLinkUserData(userData);
        frame.setLengthDomain(frameDto.getLengthDomain());
        frame.setControlDomain(frameDto.getControlDomain());
        frame.setAddressDomain(frameDto.getAddressDomain());
        frame.setHcs(hcs);
        frame.setNormalData(getResponseNormalData);
        frame.setFcs(fcs);
        return frame;
    }

    @Override
    public GetResponseNormalData parseLinkUserData(byte[] userDataBytes){
        GetResponseNormalData normalData = new GetResponseNormalData();
        log.info("用户完整数据{}", HexUtils.bytesToHex(userDataBytes));
        normalData.setApdu(userDataBytes[0]);
        byte apduByte = userDataBytes[0];
        // 这是客户端发起的请求
        ServerAPDU serverAPDU = ServerAPDU.getServerAPDUBySign(apduByte);
        if(serverAPDU != null){
            normalData.setServerAPDU(serverAPDU);
        }
        byte responseTypeByte = userDataBytes[1];
        GetResponse getResponse = GetResponse.getResponseBySign(responseTypeByte);
        normalData.setGetResponse(getResponse);

        normalData.setPIID(userDataBytes[2]);

        byte[] oadBytes = new byte[4];
        System.arraycopy(userDataBytes,3,oadBytes,0,4);
        normalData.setOadBytes(oadBytes);
        OAD oad = super.parseOAD(oadBytes);
        normalData.setOad(oad);

        GetResultType getResultType = GetResultType.getResultTypeBySign(userDataBytes[7]);
        if(getResultType != null){
            normalData.setGetResultType(getResultType);
        }
        DataType dataTypeBySign = DataType.getDataTypeBySign(userDataBytes[8]);
        if(dataTypeBySign != null){
            normalData.setDataType(dataTypeBySign);
        }
        normalData.setLength(userDataBytes[9]);

        byte[] result = new byte[normalData.getLength()];
        System.arraycopy(userDataBytes,10,result,0,normalData.getLength());
        normalData.setData(result);

        normalData.setTimeTag(userDataBytes[userDataBytes.length-1]);

        return normalData;
    }

    @Override
    public Object getData(GetResponseNormalFrame frame) {
        DataType dataType = frame.getNormalData().getDataType();
        byte[] data = frame.getNormalData().getData();
        switch (dataType){
            case STRING:
                return HexUtils.bytesToHex(data).replaceAll(" ","");
            default:
                log.error("未知的数据类型{}",dataType);
        }
        return null;
    }

}
