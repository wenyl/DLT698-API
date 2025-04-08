package cn.com.wenyl.bs.dlt698.utils;

import cn.com.wenyl.bs.dlt698.common.constants.DLT698Def;
import cn.com.wenyl.bs.dlt698.common.constants.RequestType;
import cn.com.wenyl.bs.dlt698.common.entity.AddressDomain;
import cn.com.wenyl.bs.dlt698.common.entity.ControlDomain;
import cn.com.wenyl.bs.dlt698.common.entity.Frame;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

public class FrameBuildUtils {
    /**
     * 根据帧头信息和链路用户数据构建帧的字节数据
     * @param frameHead 帧头信息
     * @param linkUserData 链路用户数据
     * @return 返回帧的字节信息
     */
    public static byte[] buildFrame(byte[] frameHead, byte[] linkUserData) {
        // 构建长度域，并初始化帧头，数据长度为帧头+apdu链路用户数据长度+2字节hcs校验信息+2字节fcs校验信息
        byte[] lengthDomain = buildFrameLength(frameHead.length+linkUserData.length+4);
        frameHead[0] = lengthDomain[0];
        frameHead[1] = lengthDomain[1];
        byte[] hcs = buildHCS(frameHead);

        byte[] body = new byte[frameHead.length+2+linkUserData.length];
        System.arraycopy(frameHead, 0, body, 0, frameHead.length);
        System.arraycopy(hcs, 0, body, frameHead.length, hcs.length);
        System.arraycopy(linkUserData, 0, body, frameHead.length+hcs.length, linkUserData.length);

        byte[] fcs = buildFCS(body);

        byte[] totalFrameData = new byte[body.length+fcs.length+2];
        totalFrameData[0] = DLT698Def.START_MARK;
        System.arraycopy(body, 0, totalFrameData, 1, body.length);
        System.arraycopy(fcs, 0, totalFrameData, body.length+1, fcs.length);
        totalFrameData[totalFrameData.length-1] = DLT698Def.END_MARK;
        return totalFrameData;
    }
    /**
     * 构建长度域
     * @param length 长度
     * @return 长度域
     */
    public static byte[] buildFrameLength(int length) {
        byte[] bytes = new byte[2];
        // 低字节 (LSB)
        bytes[0] = (byte)(length & 0xFF);
        // 高字节 (MSB)
        bytes[1] = (byte)((length >> 8) & 0xFF);
        return bytes;
    }
    /**
     * 构建HCS校验信息
     * @param frameHead 帧头
     * @return HCS校验信息
     */
    public static byte[] buildHCS(byte[] frameHead) {
        ByteBuffer buffer = ByteBuffer.allocate(frameHead.length+2);
        buffer.put(frameHead);
        return FrameCheckUtils.tryCS16(buffer.array(), frameHead.length);
    }

    /**
     * 构建FCS校验信息
     * @param frameBody 帧体
     * @return FCS校验信息
     */
    public static byte[] buildFCS(byte[] frameBody) {
        ByteBuffer buffer = ByteBuffer.allocate(frameBody.length+2);
        buffer.put(frameBody);
        return FrameCheckUtils.tryCS16(buffer.array(), frameBody.length);
    }

    public static byte[] buildFrameHead(Frame frame) {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put((byte)0);
        buffer.put((byte)0);
        // 长度域
        buffer.put(buildControlDomain(frame.getControlDomain()));
        // 地址域
        buffer.put(buildAddressDomain(frame.getAddressDomain()));
        byte[] byteArray = new byte[buffer.position()];
        buffer.rewind(); // 重置位置到0
        buffer.get(byteArray); // 从缓冲区读取到数组中
        return byteArray;
    }
    /**
     * 获取控制字
     * 控制域定义：
     * bit0-bit2: 功能码(0-7)
     * bit3: 扰码标志SC
     * bit4: 保留
     * bit5: 分帧标识
     * bit6: 启动标志PRM
     * bit7: 传输方向DIR
     */
    public static byte buildControlDomain(ControlDomain controlDomain) {
        byte ctrl = 0;

        // bit0-bit2: 功能码(0-7)
        ctrl |= (byte)(controlDomain.getFunCode() & 0x07);

        // bit3: 扰码标志SC
        ctrl |= (byte)((controlDomain.getSc() & 0x01) << 3);

        // bit4: 保留，固定为0

        // bit5: 分帧标识
        ctrl |= (byte)((controlDomain.getFrameFlg() & 0x01) << 5);

        // bit6: 启动标志PRM
        ctrl |= (byte)((controlDomain.getPrm() & 0x01) << 6);

        // bit7: 传输方向DIR
        ctrl |= (byte)((controlDomain.getDir() & 0x01) << 7);

        return (byte)(ctrl & 0xFF);
    }

    /**
     * 生成地址域数据
     * @param addressDomain 服务器地址信息
     * @return 地址域数据
     */
    public static byte[] buildAddressDomain(AddressDomain addressDomain) {
        int saType = addressDomain.getAddressType();
        int la = addressDomain.getLogicAddress();
        byte[] sa = addressDomain.getServerAddress();
        byte ca = addressDomain.getClientAddress();

        // 计算地址域总长度
        int totalLength = 1; // 特征字节
        if (la > 1) {
            totalLength += 1; // 扩展逻辑地址字节
        }
        totalLength += sa.length; // 服务器地址
        totalLength += 1; // 客户机地址

        // 创建地址域数组
        byte[] addrField = new byte[totalLength];
        int offset = 0;

        // 1. 添加特征字节，服务器字节地址长度由0-15表示，所以要-1
        addrField[offset++] = generateAddressFeature(saType, la, sa.length);

        // 2. 添加扩展逻辑地址(如果有)
        if (la > 1) {
            addrField[offset++] = (byte)la;
        }

        // 3. 添加服务器地址
        System.arraycopy(sa, 0, addrField, offset, sa.length);
        offset += sa.length;

        // 4. 添加客户机地址
        addrField[offset] = ca;

        return addrField;
    }
    /**
     * 生成地址特征字节
     * @param saType 服务器地址类型(0-3)
     * @param logicAddr 逻辑地址(0-255)
     * @param saLength 服务器地址字节数(1-16)
     * @return 地址特征字节
     */
    private static byte generateAddressFeature(int saType, int logicAddr, int saLength){
        byte feature = 0;

        // 1. 设置地址类型(bit6-bit7)
        feature |= (byte)((saType & 0x03) << 6);

        // 2. 设置逻辑地址(bit4-bit5)
        if (logicAddr <= 1) {
            // 无扩展逻辑地址
            feature |= (byte)((logicAddr & 0x01) << 4);
        } else {
            // 有扩展逻辑地址
            feature |= (0x20); // 设置bit5为1
            // bit4保留为0
        }

        // 3. 设置地址字节数(bit0-bit3)
        // 地址长度 = 逻辑地址长度(0或1) + 服务器地址长度
        int totalLength = (logicAddr <= 1 ? saLength : saLength + 1);
        feature = (byte)((feature & 0xF0) | ((totalLength - 1) & 0x0F));
        return feature;
    }
    /**
     * 构建一个客户端发起的GetRequest类型请求,这个在具体的子类中实现就好
     * @param functionCode 功能码
     * @param scramblingCodeFlag sc控制标识
     * @param frameFlag 分帧标识
     * @param requestType 请求类型
     * @param addressType 地址类型
     * @param logicAddress 逻辑地址
     * @param serverAddress 服务器地址
     * @param clientAddress 客户端地址
     * @return 返回帧信息
     */
    public static <T extends Frame> T getCommonFrame(Class<T> frameClass, int functionCode, int scramblingCodeFlag,
                          int frameFlag, RequestType requestType, int addressType,
                          int logicAddress, byte[] serverAddress, byte clientAddress
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T frame = frameClass.getDeclaredConstructor().newInstance();
        ControlDomain controlDomain = new ControlDomain();
        controlDomain.setFunCode(functionCode);
        controlDomain.setSc(scramblingCodeFlag);
        controlDomain.setFrameFlg(frameFlag);
        controlDomain.setPrm(requestType.getPrm());
        controlDomain.setDir(requestType.getDir());
        frame.setControlDomain(controlDomain);

        AddressDomain addressDomain = new AddressDomain();
        addressDomain.setAddressType(addressType);
        addressDomain.setLogicAddress(logicAddress);
        addressDomain.setServerAddress(serverAddress);
        addressDomain.setClientAddress(clientAddress);
        frame.setAddressDomain(addressDomain);
        return frame;
    }
}
