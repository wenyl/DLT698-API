package cn.com.wenyl.bs.dlt698.client.entity;

import cn.com.wenyl.bs.dlt698.common.Frame;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ConnectResponse类型的frame
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConnectResponseFrame extends Frame {
    private ConnectResponseData connectResponseData;
}
