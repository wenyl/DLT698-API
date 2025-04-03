package cn.com.wenyl.bs.dlt698.server.entity;

import cn.com.wenyl.bs.dlt698.common.LinkUserData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * LinkResponse类型帧的数据部分
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LinkRequestData extends LinkUserData {
    // 心跳周期
    private byte[] heartbeatIntervalBytes;
    private int heartbeatInterval;
    // 请求时间
    private byte[] requestTime;
}
