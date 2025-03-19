package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 返回值类型
 */
@Getter
public enum DataType {
    STRING(String.class,(byte)0x09,"数据类型：oct-string");
    private final Class aClass;
    private final byte sign;
    private final String desc;
    DataType(Class aClass,byte sign, String desc){
        this.aClass = aClass;
        this.sign = sign;
        this.desc = desc;
    }

    public static DataType getDataTypeBySign(byte sign){
        DataType[] values = DataType.values();
        for(DataType dataType:values) {
            if (dataType.getSign() == sign) {
                return dataType;
            }
        }
        return null;
    }
}
