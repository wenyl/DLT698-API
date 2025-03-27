package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

@Getter
public enum TimeUnit {
    SECOND((byte)0x00,0,"秒",1),
    MINUTE((byte)0x01,1,"分",1),
    HOUR((byte)0x02,2,"时",1),
    DAY((byte)0x03,3,"日",1),
    MONTH((byte)0x04,4,"月",1),
    YEAR((byte)0x05,5,"年",1);
    private final byte sign;
    private final int signNum;
    private final String desc;
    private final int length;
    TimeUnit(byte sign, int signNum, String desc,int length){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
        this.length = length;
    }

    public static TimeUnit getTimeUnitBySign(byte sign){
        TimeUnit[] values = TimeUnit.values();
        for(TimeUnit timeUnit:values) {
            if (timeUnit.getSign() == sign) {
                return timeUnit;
            }
        }
        return null;
    }
}
