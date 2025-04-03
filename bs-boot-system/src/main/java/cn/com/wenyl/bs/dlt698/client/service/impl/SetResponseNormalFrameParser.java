package cn.com.wenyl.bs.dlt698.client.service.impl;

import cn.com.wenyl.bs.dlt698.client.constants.DARType;
import cn.com.wenyl.bs.dlt698.client.constants.ServerAPDU;
import cn.com.wenyl.bs.dlt698.client.constants.SetResponse;
import cn.com.wenyl.bs.dlt698.common.OAD;
import cn.com.wenyl.bs.dlt698.client.entity.SetResponseNormalData;
import cn.com.wenyl.bs.dlt698.client.entity.SetResponseNormalFrame;

import cn.com.wenyl.bs.dlt698.client.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.client.service.BaseFrameParser;
import cn.com.wenyl.bs.dlt698.common.BaseFrameParserImpl;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("setResponseNormalFrameParser")
public class SetResponseNormalFrameParser extends BaseFrameParserImpl<SetResponseNormalFrame, SetResponseNormalData> implements BaseFrameParser<SetResponseNormalFrame,SetResponseNormalData> {
    @Override
    public SetResponseNormalFrame parseFrame(byte[] frameBytes) throws RuntimeException {
        SetResponseNormalFrame frame  = new SetResponseNormalFrame();
        if(!super.checkFrame(frameBytes)){
            String errorInfo = "无效帧：起始符或结束符错误,当前帧起始符--"+ HexUtils.byteToHex(frameBytes[0])+",结束符--"+HexUtils.byteToHex(frameBytes[frameBytes.length-1]);
            log.error(errorInfo);
            throw new RuntimeException(errorInfo);
        }
        FrameDto frameDto = super.getFrameDto(frameBytes);
        SetResponseNormalData setResponseNormalData = this.parseLinkUserData(frameDto.getUserData());
        frame.setLengthDomain(frameDto.getLengthDomain());
        frame.setControlDomain(frameDto.getControlDomain());
        frame.setAddressDomain(frameDto.getAddressDomain());
        frame.setHcs(frameDto.getHcs());
        frame.setData(setResponseNormalData);
        frame.setFcs(frameDto.getFcs());
        return frame;
    }

    @Override
    public SetResponseNormalData parseLinkUserData(byte[] userDataBytes) {
        SetResponseNormalData data = new SetResponseNormalData();
        log.info("用户完整数据{}", HexUtils.bytesToHex(userDataBytes));
        data.setApdu(userDataBytes[0]);
        byte apduByte = userDataBytes[0];
        // 服务端响应类型
        ServerAPDU serverAPDU = ServerAPDU.getServerAPDUBySign(apduByte);
        if(serverAPDU != null){
            data.setServerAPDU(serverAPDU);
        }
        byte responseTypeByte = userDataBytes[1];
        SetResponse setResponse = SetResponse.getSetResponseBySign(responseTypeByte);
        data.setSetResponse(setResponse);

        data.setPIID(userDataBytes[2]);
        byte[] oadBytes = new byte[4];
        System.arraycopy(userDataBytes,3,oadBytes,0,4);
        data.setOadBytes(oadBytes);
        OAD oad = super.parseOAD(oadBytes);
        data.setOad(oad);
        DARType darType = DARType.getDARBySign(userDataBytes[7]);
        if(darType == null){
            String msg = "未知数据类型"+HexUtils.byteToHex(userDataBytes[7]);
            log.error(msg);
            throw new RuntimeException(msg);
        }
        if(!darType.equals(DARType._0)){
            String msg = "设置数据错误"+darType.getDesc();
            log.info(msg);
            throw new RuntimeException(msg);
        }
        data.setDarType(darType);
        data.setFollowReport(userDataBytes[8]);
        data.setTimeTag(userDataBytes[9]);
        return data;
    }

    @Override
    public Object getData(SetResponseNormalFrame frame) throws RuntimeException {
        if(frame.getData().getDarType().equals(DARType._0)){
            return "设置成功";
        }
        return frame.getData().getDarType().getDesc();
    }
}
