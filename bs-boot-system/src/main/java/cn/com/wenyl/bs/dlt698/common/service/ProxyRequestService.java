package cn.com.wenyl.bs.dlt698.common.service;

/**
 * 代理请求接口
 */
public interface ProxyRequestService {
    /**
     * 代理请求
     * @param deviceIp 设备IP
     * @param frame 被代理的请求(一个完整的请求帧数据)
     */
    void proxyCmd(String deviceIp,byte[] frame)  throws Exception;

    /**
     * 代理设置请求
     * @param deviceIp 设备IP
     * @param proxyFrame 代理帧
     * @param setData 设置的数据
     * @throws Exception 异常
     */
    void setProxyCmd(String deviceIp,byte[] proxyFrame,String setData) throws Exception;
}
