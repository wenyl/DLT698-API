package cn.com.wenyl.bs.dlt698.utils;

import cn.com.wenyl.bs.dlt698.common.constants.AttrNum;
import cn.com.wenyl.bs.dlt698.common.constants.DLT698Def;
import cn.com.wenyl.bs.dlt698.common.constants.DataType;
import cn.com.wenyl.bs.dlt698.common.constants.OI;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.entity.AddressDomain;
import cn.com.wenyl.bs.dlt698.common.entity.ControlDomain;
import cn.com.wenyl.bs.dlt698.common.entity.LengthDomain;
import cn.com.wenyl.bs.dlt698.common.entity.OAD;
import com.alibaba.fastjson2.JSONArray;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static cn.com.wenyl.bs.dlt698.utils.HexUtils.bytesToHex;
@Slf4j
public class FrameParseUtils {
    /**
     * 提取公用的frameDto数据，不对链路用户数据做任何处理，直接 返回
     * @param frameBytes 完整帧数据
     * @return frameDto数据
     * @throws RuntimeException 异常
     */
    public static FrameDto getFrameDto(byte[] frameBytes) throws RuntimeException{
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
    /**
     * 解析帧头数据
     * @param frameBytes 帧头数据字节信息
     * @return 帧头数据
     */
    public static FrameDto parseFrameHead(byte[] frameBytes){
        FrameDto frameDto = new FrameDto();
        // 解析长度域
        byte[] lengthBytes = Arrays.copyOfRange(frameBytes,1,3);
        LengthDomain lengthDomain = parseLengthDomain(lengthBytes);
        // 解析控制域
        byte controlByte = frameBytes[3];
        ControlDomain controlDomain = parseControlDomain(controlByte);

        // 获取地址域的字节数据并解析
        int[] offsetArray = new int[1];
        byte[] addressBytes = getAddressBytes(frameBytes,offsetArray);
        AddressDomain addressDomain = parseAddressDomain(addressBytes);

        frameDto.setLengthBytes(lengthBytes);
        frameDto.setLengthDomain(lengthDomain);
        frameDto.setControlByte(controlByte);
        frameDto.setControlDomain(controlDomain);
        frameDto.setAddressBytes(addressBytes);
        frameDto.setAddressDomain(addressDomain);
        frameDto.setOffset(offsetArray[0]);
        return frameDto;
    }
    /**
     * 获取完整帧数据中的HCS
     * @param frameBytes 完整的帧数据
     * @param offset 数组下标
     * @return HCS
     */
    public static byte[] getHCS(byte[] frameBytes,int offset){
        return Arrays.copyOfRange(frameBytes, offset+1, offset + 3);
    }
    /**
     * 获取完整帧数据中的FCS
     * @param frameBytes 完整的帧数据
     * @param offset 数组下标
     * @return FCS
     */
    public static byte[] getFCS(byte[] frameBytes,int offset){
        return Arrays.copyOfRange(frameBytes, offset+1, offset + 3);
    }
    /**
     * 校验HCS
     * @param frameDto 帧信息
     * @return 返回校验结果
     */
    public static boolean checkFrameHCS(FrameDto frameDto){
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
    /**
     * 校验FCS
     * @param frameDto 帧信息
     * @return 返回校验结果
     */
    public static boolean checkFrameFCS(FrameDto frameDto){
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
    /**
     * 校验HCS，根据帧头生成hcs与帧自带的hcs对比
     * @param frameHead 帧头
     * @param length 数据长度
     * @param hcs 帧数据中的hcs
     * @return 校验结果
     */
    public static boolean checkHCS(byte[] frameHead,int length,byte[] hcs){
        log.info("帧头数据:{}", bytesToHex(frameHead));
        byte[] calHCS = FrameCheckUtils.tryCS16(frameHead,length);
        if(calHCS.length != 0 && calHCS[0] == hcs[0] && calHCS[1] == hcs[1]){
            log.info("帧HCS校验成功");
            return true;
        }
        return false;
    }
    /**
     * 校验FCS，根据帧头生成fcs与帧自带的fcs对比
     * @param frameData 帧体数据
     * @param length 长度
     * @param fcs 帧数据中的fcs
     * @return 校验结果
     */
    public static boolean checkFCS(byte[] frameData,int length,byte[] fcs){
        log.info("帧数据{}", bytesToHex(frameData));
        byte[] calFCS = FrameCheckUtils.tryCS16(frameData,length);
        if(calFCS.length != 0 && calFCS[0] == fcs[0] && calFCS[1] == fcs[1]){
            log.info("帧FCS校验成功");
            return true;
        }
        return false;
    }
    /**
     * 校验帧
     * @param frameBytes 帧数据
     * @return 校验结果
     * @throws RuntimeException 异常信息
     */
    public static boolean checkFrame(byte[] frameBytes){
        if(frameBytes==null || frameBytes.length==0)return false;
        return frameBytes[0] == 0x68 || frameBytes[frameBytes.length - 1] == 0x16;
    }

    /**
     * 获取用户数据
     * @param frameBytes 完整帧数据
     * @param offset 用户数据开始下标
     * @return 用户字节数据
     */
    public static byte[] getUserData(byte[] frameBytes,int offset){
        return Arrays.copyOfRange(frameBytes, offset + 1, frameBytes.length - 3);
    }
    /**
     * 解析帧的长度域数据
     * @param lengthBytes 用户长度域数据字节信息
     * @return 长度域数据
     */
    public static LengthDomain parseLengthDomain(byte[] lengthBytes){
        LengthDomain lengthDomain = new LengthDomain();
        int length = (ByteBuffer.wrap(new byte[]{lengthBytes[1],lengthBytes[0] }).getShort() & 0x3FFF) + 2;
        boolean isKilobyteUnit = (lengthBytes[1] & 0x40) != 0;
        lengthDomain.setLength(length);
        lengthDomain.setLengthUnit(isKilobyteUnit? DLT698Def.KB:DLT698Def.BYTE);
        return lengthDomain;
    }

    /**
     * 解析帧的地址域数据
     * @param addressBytes 用户地址域数据字节信息
     * @return 地址域数据
     */
    public static AddressDomain parseAddressDomain(byte[] addressBytes){
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
    /**
     * 解析帧的控制域数据
     * @param frameBytes 用户控制域数据字节信息
     * @return 控制域数据
     */
    public static ControlDomain parseControlDomain(byte frameBytes) {
        ControlDomain ret = new ControlDomain();
        int controlField = frameBytes & 0xFF;
        int funcCode = controlField & 0x07; // 提取功能码（bit0-bit2）
        int sc = (controlField >>> 3) & 0x01; // 提取扰码标志SC（bit3）
        int frameFlag = (controlField >>> 5) & 0x01; // 分帧标识（bit5）
        int prm = (controlField >>> 6) & 0x01; // 启动标志PRM（bit6）
        int dir = (controlField >>> 7) & 0x01; // 方向标志DIR（bit7）
        ret.setFunCode(funcCode);
        ret.setSc(sc);
        ret.setFrameFlg(frameFlag);
        ret.setPrm(prm);
        ret.setDir(dir);
        return ret;
    }
    /**
     * 获取地址域A
     * 服务器地址SA由1字节地址特征和N个字节地址组成，客户机地址占一个字节，0表示不关注客户机地址
     * 1、地址特征定义：
     *   a、bit0…bit3：为地址的字节数，取值范围：0…15，对应表示1…16个字节长度
     *   b、bit4…bit5：逻辑地址；
     *         bit5=0 表示无扩展逻辑地址，bit4取值0和1分别表示逻辑地址0和1；
     *         bit5=1 表示有扩展逻辑地址，bit4备用；地址长度N包含1个字节的扩展逻辑地址，取值范围2…255，表示逻辑地址2…255；
     *   c、bit6…bit7：为服务器地址的地址类型，0 表示单地址，1 表示通配地址，2 表示组地址，3表示广播地址。
     * 2、扩展逻辑地址和地址要求如下：
     *   a、扩展逻辑地址取值范围2…255；
     *   b、编码方式为压缩BCD码，0保留；
     *   c、当服务器地址的十进制位数为奇数时，最后字节的bit3…bit0用FH表示
     * @param frameBytes 完整帧数据
     * @param offsetArray 存储地址域数据在帧数据中的结束位置
     * @return 地址域A
     */
    public static byte[] getAddressBytes(byte[] frameBytes,int[] offsetArray) {
        int addressField = frameBytes[4] & 0xFF;
        int addressLength = (addressField & 0x0F) + 1;  // bit0-bit3：地址长度

        int offset = 5 + addressLength;
        byte[] addressDomain = Arrays.copyOfRange(frameBytes,4,offset+1);
        offsetArray[0] = offset;
        return addressDomain;
    }
    /**
     * 将OAD字节信息解析为OAD实体
     * @param oad oad字节信息
     * @return OAD实体
     */
    public static OAD parseOAD(byte[] oad){
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
    public static Object getData(DataType dataType, byte[] data) throws RuntimeException{

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
    private static void checkLength(DataType dataType,byte[] data) throws RuntimeException{
        if(data.length != dataType.getLength()){
            String msg = "约定长度为"+dataType.getLength()+",当前数据"+ bytesToHex(data);
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }

    private static JSONArray parseArray(byte[] data) {
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
            Object dataX = getData(currentDataType,dataBytes);
            parsedList.add(dataX);
        }
        return parsedList;
    }
}
