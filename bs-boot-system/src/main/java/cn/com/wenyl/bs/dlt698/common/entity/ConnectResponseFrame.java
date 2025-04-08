package cn.com.wenyl.bs.dlt698.common.entity;

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
