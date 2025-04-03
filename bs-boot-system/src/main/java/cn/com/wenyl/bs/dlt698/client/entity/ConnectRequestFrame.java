package cn.com.wenyl.bs.dlt698.client.entity;

import cn.com.wenyl.bs.dlt698.common.Frame;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ConnectRequest类型的帧
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConnectRequestFrame extends Frame {
    private ConnectRequestData connectRequestData;

}
