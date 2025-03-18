package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.entity.APDU;
import cn.com.wenyl.bs.dlt698.entity.CSInfo;
import cn.com.wenyl.bs.dlt698.entity.FrameData;
import cn.com.wenyl.bs.dlt698.entity.OAD;
import cn.com.wenyl.bs.dlt698.service.UserDataParseService;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service("/userDataParseService")
public class UserDataParseServiceImpl implements UserDataParseService {
    @Override
    public JSONObject parseUserData(FrameData frameData) {
        JSONObject data = new JSONObject();
        APDU apduData = frameData.getApdu();
        CSInfo csInfo = frameData.getCsInfo();
        RequestType requestType = RequestType.getRequestType(csInfo.getDir(), csInfo.getPrm());
        if(requestType == null){
            log.error("未知的请求路径--dir={},prm={}", csInfo.getDir(), csInfo.getPrm());
            return null;
        }

        if(apduData.getClientAPDU() != null){
            // todo 这里是程序作为服务端要去响应客户端(设备)的请求，暂不做操作
//            dealClientRequest(apduData);
            log.error("暂时无法处理设备发起的请求");
            return null;
        }

        ServerAPDU serverAPDU = apduData.getServerAPDU();
        if(serverAPDU != null){
            switch (serverAPDU){
                case GET_RESPONSE:
                    byte response = apduData.getResponse();
                    GetResponse getResponse = GetResponse.getResponseBySign(response);
                    if(getResponse == null){
                        log.error("未知的Get-Response数据类型：{}", HexUtils.byteToHex(response));
                        return null;
                    }
                    switch (getResponse){
                        case GET_RESPONSE_NORMAL:
                            byte[] oadByte = apduData.getOad();
                            OAD oad = parseOAD(oadByte);
                            getObjectOneAttr(apduData.getResponseData());
                    }
                    break;
                default:
                    log.error("未知的服务端响应--{}", HexUtils.byteToHex(apduData.getApdu()));
            }
        }


        return data;
    }

    public void getObjectOneAttr(byte[] responseData) {
        byte getResultTypeByte = responseData[0];
        if(getResultTypeByte != (byte)0x01){
            log.error("用户数据解析异常{}", HexUtils.bytesToHex(responseData));
            log.error("响应结果异常{}", HexUtils.byteToHex(getResultTypeByte));
            return;
        }
        byte dataTypeByte = responseData[1];
        DataType dataType = DataType.getDataTypeBySign(dataTypeByte);
        if(dataType == null){
            log.error("用户数据解析异常{}", HexUtils.bytesToHex(responseData));
            log.error("未知数据类型{}", HexUtils.byteToHex(getResultTypeByte));
            return;
        }
        int dataLength = responseData[2];
        byte[] dataContent = new byte[responseData.length - 3];
        System.arraycopy(responseData, 3, dataContent, 0, dataLength);
        switch (dataType){
            case _09:
                //解析字符串
                log.info("数据解析成功:{}", HexUtils.bytesToHex(responseData));
                log.info("数据类型为{},数据长度为{}", dataType.getDesc(),dataLength);
                log.info("解析结果为:{}", HexUtils.bytesToHex(dataContent));
                break;
            default:
                log.error("用户数据解析异常{}", HexUtils.bytesToHex(responseData));
                log.error("未知数据类型{}", HexUtils.byteToHex(getResultTypeByte));
        }
    }


    public OAD parseOAD(byte[] oad) {
        OAD oadData = new OAD();
        byte[] sign = new byte[2];
        System.arraycopy(oad,0,sign,0,2);
        OI oiBySign = OI.getOIBySign(sign);
        if(oiBySign == null){
            log.error("oad数据解析异常{}", HexUtils.bytesToHex(oad));
            log.error("OI接口不存在{}", HexUtils.bytesToHex(sign));
            return null;
        }
        AttrNum attrNumBySign = AttrNum.getAttrNumBySign(oad[2]);
        if(attrNumBySign == null){
            log.error("oad数据解析异常{}", HexUtils.bytesToHex(oad));
            log.error("属性编号不存在{}", HexUtils.byteToHex(oad[2]));
            return null;
        }
        log.info("OAD数据解析成功{}", HexUtils.bytesToHex(oad));
        log.info("操作名--{},接口编号--{},属性编号及类型--{}", oiBySign.getDesc(), HexUtils.bytesToHex(oiBySign.getSign()), attrNumBySign.getDesc());
        oadData.setOi(oiBySign);
        oadData.setAttrNum(attrNumBySign);
        return oadData;
    }

    @Override
    public APDU getApdu(byte[] userData) {

        log.info("用户完整数据{}", HexUtils.bytesToHex(userData));
        APDU apdu = new APDU();
        apdu.setApdu(userData[0]);

        byte apduByte = userData[0];
        // 这是客户端发起的请求
        ClientAPDU clientAPDU = ClientAPDU.getClientAPDUBySign(apduByte);
        if(clientAPDU != null){
            apdu.setRequest(userData[1]);
            apdu.setClientAPDU(clientAPDU);
        }
        // 这是服务端返回的响应
        ServerAPDU serverAPDU = ServerAPDU.getServerAPDUBySign(apduByte);
        if(serverAPDU != null){
            apdu.setResponse(userData[1]);
            apdu.setServerAPDU(serverAPDU);
            int dataLength = userData.length - 9;
            byte[] result = new byte[dataLength];
            System.arraycopy(userData,7,result,0,dataLength);
            apdu.setResponseData(result);
        }
        apdu.setPiid(userData[2]);
        apdu.setTimeTag(userData[userData.length-1]);
        byte[] oad = new byte[4];
        System.arraycopy(userData,3,oad,0,4);
        apdu.setOad(oad);
        return apdu;
    }
}
