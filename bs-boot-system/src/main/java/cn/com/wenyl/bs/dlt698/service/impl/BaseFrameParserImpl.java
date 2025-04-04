package cn.com.wenyl.bs.dlt698.service.impl;

import com.alibaba.fastjson2.JSONArray;
import cn.com.wenyl.bs.dlt698.constants.AttrNum;
import cn.com.wenyl.bs.dlt698.constants.DLT698Def;
import cn.com.wenyl.bs.dlt698.constants.DataType;
import cn.com.wenyl.bs.dlt698.constants.OI;
import cn.com.wenyl.bs.dlt698.entity.*;
import cn.com.wenyl.bs.dlt698.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.service.BaseFrameParser;
import cn.com.wenyl.bs.dlt698.utils.ASN1DecoderUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameCheckUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static cn.com.wenyl.bs.dlt698.utils.HexUtils.bytesToHex;

@Slf4j
public abstract class BaseFrameParserImpl<T extends Frame,G extends LinkUserData> implements BaseFrameParser<T,G> {
    public abstract T parseFrame(byte[] frameBytes)  throws RuntimeException;
    @Override
    public FrameDto getFrameDto(byte[] frameBytes) throws RuntimeException {
        FrameDto frameDto = parseFrameHead(frameBytes);

        byte[] hcs = getHCS(frameBytes,frameDto.getOffset());
        frameDto.setHcs(hcs);
        frameDto.setOffset(frameDto.getOffset()+hcs.length);
        if(!checkFrameHCS(frameDto)){
            throw new RuntimeException("hcs校验失败"+ bytesToHex(hcs));
        }

        byte[] userData = getUserData(frameBytes,frameDto.getOffset());
        frameDto.setUserData(userData);
        frameDto.setOffset(frameDto.getOffset()+userData.length);

        byte[] fcs = getFCS(frameBytes,frameDto.getOffset());
        frameDto.setFcs(fcs);
        if(!checkFrameFCS(frameDto)){
            throw new RuntimeException("fcs校验失败"+ bytesToHex(fcs));
        }
        return frameDto;
    }
    public abstract G parseLinkUserData(byte[] userDataBytes);

    public ControlDomain parseControlDomain(byte frameBytes) {
        return null;
    }
    public LengthDomain parseLengthDomain(byte[] lengthBytes){
        LengthDomain lengthDomain = new LengthDomain();
        int length = (ByteBuffer.wrap(new byte[]{lengthBytes[1],lengthBytes[0] }).getShort() & 0x3FFF) + 2;
        boolean isKilobyteUnit = (lengthBytes[1] & 0x40) != 0;
        lengthDomain.setLength(length);
        lengthDomain.setLengthUnit(isKilobyteUnit? DLT698Def.KB:DLT698Def.BYTE);
        return lengthDomain;
    }
    public AddressDomain parseAddressDomain(byte[] addressBytes){
        AddressDomain addressDomain = new AddressDomain();
        int addressField = addressBytes[0] & 0xFF;
        int addressLength = (addressField & 0x0F) + 1;  // bit0-bit3：地址长度
        addressDomain.setAddressLength(addressLength);

        // 判断有无逻辑地址
        int hasLogicalAddress = (addressField >>> 5) & 0x01;
        byte[] extendLogicAddress;
        byte[] serverAddress;
        if (hasLogicalAddress==1) {
            extendLogicAddress = Arrays.copyOfRange(addressBytes,1,2);
            serverAddress = Arrays.copyOfRange(addressBytes,2,addressBytes.length-1);
            addressDomain.setLogicAddress(extendLogicAddress[0]);
        }else{
            serverAddress = Arrays.copyOfRange(addressBytes,1,addressBytes.length-1);
        }
        addressDomain.setServerAddress(serverAddress);
        byte clientAddress = addressBytes[addressBytes.length-1];
        addressDomain.setClientAddress(clientAddress);
        int addressType = (addressField >>> 6) & 0x03;
        addressDomain.setAddressType(addressType);
        return addressDomain;
    }
    public FrameDto parseFrameHead(byte[] frameBytes){
        FrameDto frameDto = new FrameDto();
        // 解析长度域
        byte[] lengthBytes = Arrays.copyOfRange(frameBytes,1,3);
        LengthDomain lengthDomain = this.parseLengthDomain(lengthBytes);
        // 解析控制域
        byte controlByte = frameBytes[3];
        ControlDomain controlDomain = this.parseControlDomain(controlByte);


        // 获取地址域的字节数据并解析
        int[] offsetArray = new int[1];
        byte[] addressBytes = this.getAddressBytes(frameBytes,offsetArray);
        AddressDomain addressDomain = this.parseAddressDomain(addressBytes);

        frameDto.setLengthBytes(lengthBytes);
        frameDto.setLengthDomain(lengthDomain);
        frameDto.setControlByte(controlByte);
        frameDto.setControlDomain(controlDomain);
        frameDto.setAddressBytes(addressBytes);
        frameDto.setAddressDomain(addressDomain);
        frameDto.setOffset(offsetArray[0]);
        return frameDto;
    }


    public byte[] getAddressBytes(byte[] frameBytes,int[] offsetArray) {
        int addressField = frameBytes[4] & 0xFF;
        int addressLength = (addressField & 0x0F) + 1;  // bit0-bit3：地址长度

        int offset = 5 + addressLength;
        byte[] addressDomain = Arrays.copyOfRange(frameBytes,4,offset+1);
        offsetArray[0] = offset;
        return addressDomain;
    }
    public byte[] getUserData(byte[] frameBytes,int offset){
        return Arrays.copyOfRange(frameBytes, offset + 1, frameBytes.length - 3);
    }
    public byte[] getHCS(byte[] frameBytes,int offset){
        return Arrays.copyOfRange(frameBytes, offset+1, offset + 3);
    }
    public byte[] getFCS(byte[] frameBytes,int offset){
        return Arrays.copyOfRange(frameBytes, offset+1, offset + 3);
    }
    public boolean checkFrameHCS(FrameDto frameDto){
        // 校验帧头数据
        byte[] frameHead = new byte[frameDto.getAddressBytes().length+5];
        System.arraycopy(frameDto.getLengthBytes(),0,frameHead,0,frameDto.getLengthBytes().length);
        frameHead[frameDto.getLengthBytes().length] = frameDto.getControlByte();
        System.arraycopy(frameDto.getAddressBytes(),0,frameHead,frameDto.getLengthBytes().length+1,frameDto.getAddressBytes().length);
        if(!checkHCS(frameHead,frameDto.getLengthBytes().length+1+frameDto.getAddressBytes().length,frameDto.getHcs())){
            log.error("帧HCS数据校验失败！");
            return false;
        }
        frameDto.setFrameHeadWithHcsBytes(frameHead);
        return true;
    }
    public boolean checkFrameFCS(FrameDto frameDto){
        // 校验帧数据
        int dataLength = frameDto.getFrameHeadWithHcsBytes().length+frameDto.getUserData().length;
        byte[] frameCheckData = new byte[dataLength+2];
        System.arraycopy(frameDto.getFrameHeadWithHcsBytes(),0,frameCheckData,0,frameDto.getFrameHeadWithHcsBytes().length);
        System.arraycopy(frameDto.getUserData(),0,frameCheckData,frameDto.getFrameHeadWithHcsBytes().length,frameDto.getUserData().length);
        if(!checkFCS(frameCheckData,dataLength,frameDto.getFcs())){
            log.error("帧FCS数据校验失败！");
            return false;
        }
        return true;
    }
    public boolean checkHCS(byte[] frameHead,int length,byte[] hcs){
        log.info("帧头数据:{}", bytesToHex(frameHead));
        byte[] calHCS = FrameCheckUtils.tryCS16(frameHead,length);
        if(calHCS.length != 0 && calHCS[0] == hcs[0] && calHCS[1] == hcs[1]){
            log.info("帧HCS校验成功");
            return true;
        }
        return false;
    }

    public boolean checkFCS(byte[] frameData,int length,byte[] fcs){
        log.info("帧数据{}", bytesToHex(frameData));
        byte[] calFCS = FrameCheckUtils.tryCS16(frameData,length);
        if(calFCS.length != 0 && calFCS[0] == fcs[0] && calFCS[1] == fcs[1]){
            log.info("帧FCS校验成功");
            return true;
        }
        return false;
    }
    public boolean checkFrame(byte[] frameBytes){
        if(frameBytes==null || frameBytes.length==0)return false;
        return frameBytes[0] == 0x68 || frameBytes[frameBytes.length - 1] == 0x16;
    }
    public OAD parseOAD(byte[] oad){
        OAD oadData = new OAD();
        byte[] sign = new byte[2];
        System.arraycopy(oad,0,sign,0,2);
        OI oiBySign = OI.getOIBySign(sign);
        if(oiBySign == null){
            log.error("oad数据解析异常{}", bytesToHex(oad));
            log.error("OI接口不存在{}", bytesToHex(sign));
            return null;
        }
        AttrNum attrNumBySign = AttrNum.getAttrNumBySign(oad[2]);
        if(attrNumBySign == null){
            log.error("oad数据解析异常{}", bytesToHex(oad));
            log.error("属性编号不存在{}", HexUtils.byteToHex(oad[2]));
            return null;
        }
        log.info("OAD数据解析成功{}", bytesToHex(oad));
        log.info("操作名--{},接口编号--{},属性编号及类型--{}", oiBySign.getDesc(), bytesToHex(oiBySign.getSign()), attrNumBySign.getDesc());
        oadData.setOi(oiBySign);
        oadData.setAttrNum(attrNumBySign);
        return oadData;
    }

    public abstract Object getData(T frame) throws RuntimeException;

    @Override
    public Object getData(DataType dataType,byte[] data) throws RuntimeException{

        if(dataType == null){
            String msg = "未知的数据类型"+HexUtils.bytesToHex(data);
            log.info(msg);
            throw new RuntimeException(msg);
        }
        Object ret;
        switch (dataType){
            case OCT_STRING:
                ret = bytesToHex(data);
                log.info("数据解析为{}", ret);
                return ret;
            case LONG_UNSIGNED:
                checkLength(dataType,data);
                return ASN1DecoderUtils.decodeLongUnsigned(data);
            case LONG64_UNSIGNED:
                checkLength(dataType,data);
                ret =  new BigInteger(1,data);
                return ret;
            case DOUBLE_LONG:
                checkLength(dataType,data);
                ret =  ByteBuffer.wrap(data).getInt();
                return ret;
            case DOUBLE_LONG_UNSIGNED:
                checkLength(dataType,data);
                ret =  Integer.toUnsignedLong(ByteBuffer.wrap(data).getInt());
                return ret;
            case ARRAY:
                // 各个接口的ARRAY类型返回数据值类型不一致，在各个业务方法中处理
                return parseArray(data);
            default:
                log.error("未知的数据类型{}",dataType);
        }
        return null;
    }
    private void checkLength(DataType dataType,byte[] data) throws RuntimeException{
        if(data.length != dataType.getLength()){
            String msg = "约定长度为"+dataType.getLength()+",当前数据"+ bytesToHex(data);
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }

    private JSONArray parseArray(byte[] data) {
        JSONArray parsedList = new JSONArray();
        // data[0]是数组元素个数
        int size = data[0];
        int startIndex = 1;
        // 每个数据长度
        int dataLength = (data.length-1)/size;
        for(int i=0;i<size;i++){
            byte[] currentBytes = new byte[dataLength];
            System.arraycopy(data,startIndex,currentBytes,0,dataLength);
            startIndex += dataLength;
            DataType currentDataType = DataType.getDataTypeBySign(currentBytes[0]);
            byte[] dataBytes = new byte[currentDataType.getLength()];
            System.arraycopy(currentBytes,1,dataBytes,0,dataBytes.length);
            Object dataX = this.getData(currentDataType,dataBytes);
            parsedList.add(dataX);
        }
        return parsedList;
    }

}
