package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 返回值类型
 */
@Getter
public enum DataType {
    OCT_STRING(String.class,(byte)0x09,"数据类型:octet-string,8位字节串",true),
    VISIBLE_STRING(String.class,(byte)0x0A,"数据类型:visible-string,ASCII字符串",true),
    INTEGER(Integer.class,(byte)0x0F,"数据类型:integer,8位整数",false),
    LONG64_UNSIGNED(Long.class,(byte)0x15,"数据类型:integer,8位整数",false);
    private final Class aClass;
    private final byte sign;
    private final String desc;
    private final boolean hasLength;
    DataType(Class aClass,byte sign, String desc,boolean hasLength) {
        this.aClass = aClass;
        this.sign = sign;
        this.desc = desc;
        this.hasLength = hasLength;
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
