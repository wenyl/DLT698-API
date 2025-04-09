package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.ConnectResult;
import cn.com.wenyl.bs.dlt698.common.constants.ServerAPDU;
import cn.com.wenyl.bs.dlt698.common.entity.ConnectResponseData;
import cn.com.wenyl.bs.dlt698.common.entity.ConnectResponseFrame;
import cn.com.wenyl.bs.dlt698.common.entity.dto.ConnectInfoDto;
import cn.com.wenyl.bs.dlt698.common.entity.dto.ConnectResponseDto;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.BaseFrameParser;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Service("connectResponseFrameParser")
public class ConnectResponseFrameParser implements BaseFrameParser<ConnectResponseFrame,ConnectResponseData> {
    @Override
    public ConnectResponseFrame parseFrame(byte[] frameBytes) throws RuntimeException {
        ConnectResponseFrame frame = new ConnectResponseFrame();
        if(!FrameParseUtils.checkFrame(frameBytes)){
            String errorInfo = "无效帧：起始符或结束符错误,当前帧起始符--"+ HexUtils.byteToHex(frameBytes[0])+",结束符--"+HexUtils.byteToHex(frameBytes[frameBytes.length-1]);
            log.error(errorInfo);
            throw new RuntimeException(errorInfo);
        }
        FrameDto frameDto = FrameParseUtils.getFrameDto(frameBytes);
        ConnectResponseData userData = this.parseLinkUserData(frameDto.getUserData());
        frame.setLengthDomain(frameDto.getLengthDomain());
        frame.setControlDomain(frameDto.getControlDomain());
        frame.setAddressDomain(frameDto.getAddressDomain());
        frame.setHcs(frameDto.getHcs());
        frame.setConnectResponseData(userData);
        frame.setFcs(frameDto.getFcs());
        return frame;
    }

    @Override
    public ConnectResponseData parseLinkUserData(byte[] userDataBytes) {
        ConnectResponseData connectResponseData = new ConnectResponseData();
        log.info("用户完整数据{}", HexUtils.bytesToHex(userDataBytes));
        int offset = 0;
        connectResponseData.setApdu(userDataBytes[offset]);
        byte apduByte = userDataBytes[offset];
        // 这是客户端发起的请求
        ServerAPDU serverAPDU = ServerAPDU.getServerAPDUBySign(apduByte);
        if(serverAPDU != null){
            connectResponseData.setServerAPDU(serverAPDU);
        }
        offset++;

        connectResponseData.setPIID(userDataBytes[offset]);
        offset++;

        byte[] oemId = new byte[4];
        System.arraycopy(userDataBytes,offset,oemId,0,4);
        offset+=4;

        byte[] softwareVersion = new byte[4];
        System.arraycopy(userDataBytes,offset,softwareVersion,0,4);
        offset+=4;

        byte[] softwareDate = new byte[6];
        System.arraycopy(userDataBytes,offset,softwareDate,0,6);
        offset+=6;

        byte[] hardwareVersion = new byte[4];
        System.arraycopy(userDataBytes,offset,hardwareVersion,0,4);
        offset+=4;

        byte[] hardwareDate = new byte[6];
        System.arraycopy(userDataBytes,offset,hardwareDate,0,6);
        offset+=6;

        byte[] oemExtend = new byte[8];
        System.arraycopy(userDataBytes,offset,oemExtend,0,8);
        offset+=8;

        byte[] protocolVersion = new byte[2];
        System.arraycopy(userDataBytes,offset,protocolVersion,0,2);
        offset+=2;

        byte[] protocolConformance = new byte[8];
        System.arraycopy(userDataBytes,offset,protocolConformance,0,8);
        offset+=8;

        byte[] functionConformance = new byte[16];
        System.arraycopy(userDataBytes,offset,functionConformance,0,16);
        offset+=16;

        byte[] maxSendPerFrame = new byte[2];
        System.arraycopy(userDataBytes,offset,maxSendPerFrame,0,2);
        offset+=2;

        byte[] maxReceivePerFrame = new byte[2];
        System.arraycopy(userDataBytes,offset,maxReceivePerFrame,0,2);
        offset+=2;

        byte[] maxFrameNum = new byte[1];
        System.arraycopy(userDataBytes,offset,maxFrameNum,0,1);
        offset+=1;

        byte[] maxApduSize = new byte[2];
        System.arraycopy(userDataBytes,offset,maxApduSize,0,2);
        offset+=2;

        byte[] connectTimeout = new byte[4];
        System.arraycopy(userDataBytes,offset,connectTimeout,0,4);
        offset+=4;

        byte[] connectResult = new byte[1];
        System.arraycopy(userDataBytes,offset,connectResult,0,1);
        offset+=1;
        this.isSuccess(connectResult[0]);

        byte[] securityData = new byte[1];
        System.arraycopy(userDataBytes,offset,securityData,0,1);

        connectResponseData.setOemId(oemId);
        connectResponseData.setSoftwareVersion(softwareVersion);
        connectResponseData.setSoftwareDate(softwareDate);
        connectResponseData.setHardwareVersion(hardwareVersion);
        connectResponseData.setHardwareDate(hardwareDate);
        connectResponseData.setOemExtend(oemExtend);
        connectResponseData.setConnectResult(connectResult);
        connectResponseData.setSecurityData(securityData);

        ConnectInfoDto ci = new ConnectInfoDto();
        ci.setProtocolVersion(protocolVersion);
        ci.setProtocolConformance(protocolConformance);
        ci.setFunctionConformance(functionConformance);
        ci.setMaxSendPerFrame(maxSendPerFrame);
        ci.setMaxReceivePerFrame(maxReceivePerFrame);
        ci.setMaxApduSize(maxApduSize);
        ci.setMaxFrameNum(maxFrameNum);
        ci.setConnectTimeout(connectTimeout);
        connectResponseData.setConnectInfo(ci);
        return connectResponseData;
    }

    private void isSuccess(byte b) throws RuntimeException{
        ConnectResult connectResult = ConnectResult.getConnectResultBySign(b);
        if (Objects.requireNonNull(connectResult) == ConnectResult.SUCCESS) {
            return;
        }
        log.error(connectResult.getDesc());
        throw new RuntimeException(connectResult.getDesc());
    }

    @Override
    public Object getData(ConnectResponseFrame frame) throws RuntimeException {
        ConnectResponseDto dto = new ConnectResponseDto();
        ConnectResponseData connectResponseData = frame.getConnectResponseData();
        dto.setOemID(new String(connectResponseData.getOemId(), StandardCharsets.US_ASCII));
        dto.setSoftwareVersion(new String(connectResponseData.getSoftwareVersion(), StandardCharsets.US_ASCII));
        dto.setSoftwareDate(new String(connectResponseData.getSoftwareDate(), StandardCharsets.US_ASCII));
        dto.setHardwareVersion(new String(connectResponseData.getHardwareVersion(), StandardCharsets.UTF_8));
        dto.setHardwareDate(new String(connectResponseData.getHardwareDate(), StandardCharsets.UTF_8));
        dto.setGetOemExtend(new String(connectResponseData.getOemExtend(), StandardCharsets.US_ASCII));
        return dto;
    }
}
