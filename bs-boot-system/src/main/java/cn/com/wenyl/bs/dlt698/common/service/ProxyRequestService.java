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
}
