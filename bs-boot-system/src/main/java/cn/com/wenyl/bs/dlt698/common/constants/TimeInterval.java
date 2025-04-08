package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 碳表用来计算碳排放数据的时间间隔
 */
@Getter
public enum TimeInterval {
    ONE_HOUR("1小时一次",new byte[]{(byte)0x00,(byte)0x01}),
    FIFTEEN_MINUTE("15分钟一次",new byte[]{(byte)0x00,(byte)0x0F});
    private final String desc;
    private final byte[] sign;
    TimeInterval(String desc,byte[] sign){
        this.desc = desc;
        this.sign = sign;
    }
}
