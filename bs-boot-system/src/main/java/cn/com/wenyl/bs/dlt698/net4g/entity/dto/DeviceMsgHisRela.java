package cn.com.wenyl.bs.dlt698.net4g.entity.dto;

import cn.com.wenyl.bs.dlt698.net4g.entity.DeviceMsgHis;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 这个表用于存储父类消息对应的消息查询条件
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceMsgHisRela extends DeviceMsgHis {
    /**
     * 父类消息dir对应dir
     */
    private Integer targetDir;
    /**
     * 父类消息对应的prm
     */
    private Integer targetPrm;
    /**
     * 父类消息对应的apdu
     */
    private String targetApdu;
    /**
     * 父类消息对应的apduOpera
     */
    private String targetApduOpera;
}
