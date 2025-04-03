package cn.com.wenyl.bs.dlt698.client.service;


import cn.com.wenyl.bs.dlt698.common.AddressDomain;

/**
 * 地址域构建服务
 */
public interface AddressDomainBuildService {

    /**
     * 生成地址特征字节
     * @param saType 服务器地址类型(0-3)
     * @param logicAddr 逻辑地址(0-255)
     * @param saLength 服务器地址字节数(1-16)
     * @return 地址特征字节
     */
    byte generateAddressFeature(int saType, int logicAddr, int saLength);

    /**
     * 生成服务器地址
     * @param address 十进制地址字符串
     * @return 服务器地址字节数组
     */
    byte[] generateServerAddress(String address);

    /**
     * 生成地址域数据
     * @param addressDomain 服务器地址信息
     * @return 地址域数据
     */
    byte[] buildAddressDomain(AddressDomain addressDomain);

}
