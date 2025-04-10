package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.ControlDomain;
import cn.com.wenyl.bs.dlt698.common.entity.OAD;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.entity.DeviceMsgHis;
import cn.com.wenyl.bs.dlt698.net4g.entity.dto.DeviceMsgHisRela;
import cn.com.wenyl.bs.dlt698.net4g.mapper.DeviceMsgHisMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceMsgHisService;
import cn.com.wenyl.bs.dlt698.net4g.service.StationDeviceMsgRelaService;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备消息历史 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Slf4j
@Service
public class DeviceMsgHisServiceImpl extends ServiceImpl<DeviceMsgHisMapper, DeviceMsgHis> implements DeviceMsgHisService {

    @Resource
    private StationDeviceMsgRelaService deviceMsgRelaService;

    @Override
    public void save(FrameDto frameDto, Integer deviceId, byte[] bytes) throws RuntimeException{
        // 关联的消息条件
        DeviceMsgHisRela relaMsg = new DeviceMsgHisRela();

        // 解析数据
        byte[] userData = frameDto.getUserData();
        ClientAPDU clientAPDU = frameDto.getClientAPDU();
        ServerAPDU serverAPDU = frameDto.getServerAPDU();
        String apdu = frameDto.getClientAPDU() == ClientAPDU.UNKNOWN ?
                HexUtils.byteToHex(serverAPDU.getSign()):HexUtils.byteToHex(clientAPDU.getSign());
        ControlDomain controlDomain = frameDto.getControlDomain();

        DeviceMsgHis deviceMsgHis = new DeviceMsgHis();
        deviceMsgHis.setDeviceId(deviceId);
        deviceMsgHis.setDir(controlDomain.getDir());
        deviceMsgHis.setPrm(controlDomain.getPrm());
        deviceMsgHis.setApdu(apdu);
        if(controlDomain.getDir() == 1 && controlDomain.getPrm() == 0){
            relaMsg.setTargetDir(0);
            relaMsg.setTargetPrm(0);
        }
        if(controlDomain.getDir() == 0 && controlDomain.getPrm() == 1){
            relaMsg.setTargetDir(1);
            relaMsg.setTargetPrm(1);
        }
        switch (clientAPDU){
            case LINK_REQUEST:
                relaMsg.setApdu(HexUtils.byteToHex(ServerAPDU.LINK_RESPONSE.getSign()));
                break;
            case GET_REQUEST:
                relaMsg.setApdu(HexUtils.byteToHex(ServerAPDU.GET_RESPONSE.getSign()));
                GetRequest getRequest = GetRequest.getRequestBySign(userData[1]);
                deviceMsgHis.setApduOpera(HexUtils.byteToHex(getRequest.getSign()));
                switch (getRequest){
                    case GET_REQUEST_NORMAL:
                        byte[] oadBytes = new byte[4];
                        System.arraycopy(userData,3,oadBytes,0,4);
                        OAD oad = FrameParseUtils.parseOAD(oadBytes);
                        String oiStr = HexUtils.bytesToHex(oad.getOi().getSign());
                        String attrNumStr = HexUtils.byteToHex(oad.getAttrNum().getSign());
                        String attrIndexStr = HexUtils.byteToHex(oad.getAttributeIndex().getSign());
                        deviceMsgHis.setOi(oiStr);
                        deviceMsgHis.setAttrNum(attrNumStr);
                        deviceMsgHis.setAttrIndex(attrIndexStr);
                        relaMsg.setApduOpera(HexUtils.byteToHex(GetResponse.GET_RESPONSE_NORMAL.getSign()));
                        relaMsg.setOi(oiStr);
                        relaMsg.setAttrNum(attrNumStr);
                        relaMsg.setAttrIndex(attrIndexStr);
                        break;
                    case UNKNOWN:
                        throw new RuntimeException("未知GetRequest类型"+HexUtils.byteToHex(getRequest.getSign()));
                }
                break;
            case SET_REQUEST:
                relaMsg.setApdu(HexUtils.byteToHex(ServerAPDU.SET_RESPONSE.getSign()));
                SetRequest setRequest = SetRequest.getSetRequestBySign(userData[1]);
                deviceMsgHis.setApduOpera(HexUtils.byteToHex(setRequest.getSign()));
                switch (setRequest){
                    case SET_REQUEST_NORMAL:
                        byte[] oadBytes = new byte[4];
                        System.arraycopy(userData,3,oadBytes,0,4);
                        OAD oad = FrameParseUtils.parseOAD(oadBytes);
                        String oiStr = HexUtils.bytesToHex(oad.getOi().getSign());
                        String attrNumStr = HexUtils.byteToHex(oad.getAttrNum().getSign());
                        String attrIndexStr = HexUtils.byteToHex(oad.getAttributeIndex().getSign());
                        deviceMsgHis.setOi(oiStr);
                        deviceMsgHis.setAttrNum(attrNumStr);
                        deviceMsgHis.setAttrIndex(attrIndexStr);
                        relaMsg.setApduOpera(HexUtils.byteToHex(SetResponse.SET_RESPONSE_NORMAL.getSign()));
                        relaMsg.setOi(oiStr);
                        relaMsg.setAttrNum(attrNumStr);
                        relaMsg.setAttrIndex(attrIndexStr);
                        break;
                    case UNKNOWN:
                        throw new RuntimeException("未知SetRequest类型"+HexUtils.byteToHex(setRequest.getSign()));
                }
                break;
        }

        switch(serverAPDU){
            case LINK_RESPONSE:
                 relaMsg.setApduOpera(HexUtils.byteToHex(ClientAPDU.LINK_REQUEST.getSign()));
                 break;
            case GET_RESPONSE:
                relaMsg.setApduOpera(HexUtils.byteToHex(ClientAPDU.GET_REQUEST.getSign()));
                GetResponse getResponse = GetResponse.getResponseBySign(userData[1]);
                deviceMsgHis.setApduOpera(HexUtils.byteToHex(getResponse.getSign()));
                switch (getResponse){
                    case GET_RESPONSE_NORMAL:
                        byte[] oadBytes = new byte[4];
                        System.arraycopy(userData,3,oadBytes,0,4);
                        OAD oad = FrameParseUtils.parseOAD(oadBytes);
                        String oiStr = HexUtils.bytesToHex(oad.getOi().getSign());
                        String attrNumStr = HexUtils.byteToHex(oad.getAttrNum().getSign());
                        String attrIndexStr = HexUtils.byteToHex(oad.getAttributeIndex().getSign());
                        deviceMsgHis.setOi(oiStr);
                        deviceMsgHis.setAttrNum(attrNumStr);
                        deviceMsgHis.setAttrIndex(attrIndexStr);
                        relaMsg.setApduOpera(HexUtils.byteToHex(GetRequest.GET_REQUEST_NORMAL.getSign()));
                        relaMsg.setOi(oiStr);
                        relaMsg.setAttrNum(attrNumStr);
                        relaMsg.setAttrIndex(attrIndexStr);
                        break;
                    case UNKNOWN:
                        throw new RuntimeException("未知GetResponse类型"+HexUtils.byteToHex(getResponse.getSign()));
                }
                break;
            case SET_RESPONSE:
                relaMsg.setApduOpera(HexUtils.byteToHex(ClientAPDU.SET_REQUEST.getSign()));
                SetResponse setResponse = SetResponse.getSetResponseBySign(userData[1]);
                deviceMsgHis.setApduOpera(HexUtils.byteToHex(setResponse.getSign()));
                switch (setResponse){
                    case SET_RESPONSE_NORMAL:
                        byte[] oadBytes = new byte[4];
                        System.arraycopy(userData,3,oadBytes,0,4);
                        OAD oad = FrameParseUtils.parseOAD(oadBytes);
                        String oiStr = HexUtils.bytesToHex(oad.getOi().getSign());
                        String attrNumStr = HexUtils.byteToHex(oad.getAttrNum().getSign());
                        String attrIndexStr = HexUtils.byteToHex(oad.getAttributeIndex().getSign());
                        deviceMsgHis.setOi(oiStr);
                        deviceMsgHis.setAttrNum(attrNumStr);
                        deviceMsgHis.setAttrIndex(attrIndexStr);
                        relaMsg.setApduOpera(HexUtils.byteToHex(SetRequest.SET_REQUEST_NORMAL.getSign()));
                        relaMsg.setOi(oiStr);
                        relaMsg.setAttrNum(attrNumStr);
                        relaMsg.setAttrIndex(attrIndexStr);
                        break;
                    case UNKNOWN:
                        throw new RuntimeException("未知SetResponse类型"+HexUtils.byteToHex(setResponse.getSign()));
                }
                break;
        }

        deviceMsgHis.setByteData(HexUtils.bytesToHex(bytes));
        deviceMsgHis.setDataLength(bytes.length);
        deviceMsgHis.setCreateTime(LocalDateTime.now());
        this.save(deviceMsgHis);
        relaMsg.setId(deviceMsgHis.getId());
        deviceMsgRelaService.saveRelaMsg(relaMsg);
    }
}
