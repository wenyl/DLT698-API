package cn.com.wenyl.bs.dlt698.service.impl;



import cn.com.wenyl.bs.dlt698.entity.APDU;
import cn.com.wenyl.bs.dlt698.entity.CSInfo;
import cn.com.wenyl.bs.dlt698.entity.FrameData;
import cn.com.wenyl.bs.dlt698.service.*;
import cn.com.wenyl.bs.dlt698.utils.CSInfoUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameCheckUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Slf4j
@Service("frameParseService")
public class FrameParseServiceImpl implements FrameParseService {
    @Resource
    private ControlDomainParseService controlDomainParseService;
    @Resource
    private AddressDomainParseService addressDomainParseService;
    @Resource
    private UserDataParseService userDataParseService;

    /**
     * 帧头校验HCS为2字节，是对帧头部分不包含起始字符和HCS本身的所有字节的校验
     * @param frameHead  帧头数据+2个空格位
     * @param length  帧头数据实际长度
     * @param hcs  frame中的帧头校验部分
     * @return 校验结果
     */
    private boolean checkHCS(byte[] frameHead,int length,byte[] hcs){
        log.info("帧头数据:{}", HexUtils.bytesToHex(frameHead));
        byte[] calHCS = FrameCheckUtils.tryCS16(frameHead,length);
        if(calHCS.length != 0 && calHCS[0] == hcs[0] && calHCS[1] == hcs[1]){
            log.info("帧HCS校验成功");
            return true;
        }
        return false;
    }

    /**
     * 帧校验FCS为2字节，是对整帧不包含起始字符、结束字符和FCS本身的所有字节的校验
     * @param frameData  帧数据
     * @param length  帧数据实际长度
     * @param fcs  frame中的帧校验部分
     * @return 校验结果
     */
    private boolean checkFCS(byte[] frameData,int length,byte[] fcs){
        byte[] calFCS = FrameCheckUtils.tryCS16(frameData,length);
        if(calFCS.length != 0 && calFCS[0] == fcs[0] && calFCS[1] == fcs[1]){
            log.info("帧FCS校验成功");
            return true;
        }
        return false;
    }



    /**
     * 获取长度域L
     * 长度域占2个字节 低字节在前，高字节在后，所以要反转一下,然后加上首尾两个字节
     * 0-13位是用户数据长度
     * bit14：帧数据长度单位，0表示帧数据长度单位为字节，1表示帧数据长度单位为千字节；
     * 15是保留位
     * @param frameData 完整帧数据
     * @return 长度域
     */
    private byte[] getLengthDomain(byte[] frameData){
        byte[] lengthDomain = Arrays.copyOfRange(frameData,1,3);
        int length = (ByteBuffer.wrap(new byte[]{frameData[2],frameData[1] }).getShort() & 0x3FFF) + 2;
        boolean isKilobyteUnit = (frameData[2] & 0x40) != 0;

//        System.out.println("帧长度数据: "+ HexUtils.bytesToHex(lengthDomain));
//        System.out.println("帧长度: " + length + (isKilobyteUnit ? " KB" : " Bytes"));
        return lengthDomain;
    }

    @Override
    public FrameData parseFrame(byte[] frame) {
        if(frame[0] != 0x68 && frame[frame.length-1] != 0x16){
            log.error("无效帧：起始符或结束符错误");
            return null;
        }
        CSInfo csInfo = new CSInfo();
        byte[] lengthDomain = getLengthDomain(frame);

        byte controlDomain = getControlDomain(frame);
        CSInfo controlCSInfo = controlDomainParseService.parseControlDomain(controlDomain);
        CSInfoUtils.copyControlDomainInfo(csInfo,controlCSInfo);

        int[] offsetArray = new int[1];
        byte[] addressDomain = getAddressDomain(frame,offsetArray);
        CSInfo addressCSInfo = addressDomainParseService.parseAddressDomain(addressDomain);
        CSInfoUtils.copyAddressDomainInfo(csInfo,addressCSInfo);
        int offset = offsetArray[0];

        byte[] hcs = getHCS(frame,offset);

        // 校验帧头数据
        byte[] frameHead = new byte[addressDomain.length+5];
        System.arraycopy(lengthDomain,0,frameHead,0,lengthDomain.length);
        frameHead[lengthDomain.length] = controlDomain;
        System.arraycopy(addressDomain,0,frameHead,lengthDomain.length+1,addressDomain.length);
        if(!checkHCS(frameHead,lengthDomain.length+1+addressDomain.length,hcs)){
            log.error("帧HCS数据校验失败！");
            return null;
        }

        byte[] userData = getUserData(frame,offset);
        // 校验帧数据
        int dataLength = frameHead.length+userData.length;
        byte[] frameCheckData = new byte[dataLength+2];
        System.arraycopy(frameHead,0,frameCheckData,0,frameHead.length);
        System.arraycopy(userData,0,frameCheckData,frameHead.length,userData.length);
        byte[] fcs = getFCS(frame);
        if(!checkFCS(frameCheckData,dataLength,fcs)){
            log.error("帧FCS数据校验失败！");
        }
        APDU apdu = userDataParseService.getApdu(userData);
        FrameData frameData = new FrameData();
        frameData.setApdu(apdu);
        frameData.setCsInfo(csInfo);
        return frameData;
    }


    /**
     * 获取帧校验FCS
     * 帧的最后两位，帧校验FCS为2字节
     * @param frame 帧数据
     * @return 帧校验FCS
     */
    private byte[] getFCS(byte[] frame) {
        byte[] fcs = Arrays.copyOfRange(frame, frame.length - 3, frame.length - 1);
        log.info("帧校验FCS: " + HexUtils.bytesToHex(fcs));
        return fcs;
    }

    /**
     * 获取用户数据
     * 从帧头校验一直到帧的最后两个字节，都是链路用户数据，包含一个APDU或APDU分帧片段
     * @param frame 帧数据
     * @param offset 下标，这里是地址域结束的位置
     * @return 用户数据
     */
    private byte[] getUserData(byte[] frame, int offset) {
        return Arrays.copyOfRange(frame, offset + 3, frame.length - 3);
    }

    /**
     * 获取帧头校验HCS
     * 帧头校验HCS为2字节，是对帧头部分不包含起始字符和HCS本身的所有字节的校验
     * @param frame 帧数据
     * @param offset 下标，这里是地址域结束的位置
     * @return 帧头校验HCS
     */
    private byte[] getHCS(byte[] frame,int offset) {
        byte[] hcs =  Arrays.copyOfRange(frame, offset+1, offset + 3);
        log.info("帧头校验HCS: {}", HexUtils.bytesToHex(hcs));
        return hcs;
    }

    /**
     * 获取控制域C
     * 控制域占1个字节
     * 0-2位标识功能码
     * 3位是扰码标志SC
     * 4位是保留位
     * 5位是分帧标识
     * 6位是启动标志PRM
     * 7位是方向标志DIR
     * @param frame 完整帧数据
     * @return 控制域C
     */
    private byte getControlDomain(byte[] frame) {
        return frame[3];
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
     * @param frame 完整帧数据
     * @return 地址域A
     */
    private byte[] getAddressDomain(byte[] frame,int[] offsetArray) {
        int addressField = frame[4] & 0xFF;
        int addressLength = (addressField & 0x0F) + 1;  // bit0-bit3：地址长度

        int offset = 5 + addressLength;
        byte[] addressDomain = Arrays.copyOfRange(frame,4,offset+1);
        offsetArray[0] = offset;
        return addressDomain;
    }

}
