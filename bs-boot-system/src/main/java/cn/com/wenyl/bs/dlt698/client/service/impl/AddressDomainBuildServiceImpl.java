package cn.com.wenyl.bs.dlt698.client.service.impl;


import cn.com.wenyl.bs.dlt698.common.AddressDomain;
import cn.com.wenyl.bs.dlt698.client.service.AddressDomainBuildService;
import org.springframework.stereotype.Service;

@Service("addressDomainBuildService")
public class AddressDomainBuildServiceImpl implements AddressDomainBuildService {
    @Override
    public byte[] buildAddressDomain(AddressDomain addressDomain) {
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

    @Override
    public byte generateAddressFeature(int saType, int logicAddr, int saLength) {
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

    @Override
    public byte[] generateServerAddress(String address) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("地址不能为空");
        }

        // 计算需要的字节数
        int length = (address.length() + 1) / 2;
        byte[] sa = new byte[length];

        // 转换为压缩BCD码
        for (int i = 0; i < address.length(); i++) {
            int digit = Character.digit(address.charAt(i), 10);
            if (digit < 0) {
                throw new IllegalArgumentException("地址必须为数字");
            }

            if (i % 2 == 0) {
                // 高4位
                sa[i/2] = (byte)(digit << 4);
            } else {
                // 低4位
                sa[i/2] |= (byte)digit;
            }
        }

        // 处理奇数位地址
        if (address.length() % 2 != 0) {
            // 最后字节的低4位用F填充
            sa[length - 1] |= 0x0F;
        }

        return sa;
    }


}
