package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 返回值类型
 * length字段标识这个类型的数据长度，固定长度的数据类型值为具体字节长度，不固定类型的长度为0(此类数据在类型后会单独有一个字节标识数据长度)
 * 这里要注意structure类型的长度指的是数据项数，array类型的长度指的是数组长度
 */
@Getter
public enum DataType {
    ARRAY(ArrayList.class,(byte)0x01,"数组的元素在对象属性或方法的描述中定义",0),
    STRUCTURE(Object.class,(byte)0x02,"结构的元素在对象属性或方法的描述中定义",0),
    OCT_STRING(String.class,(byte)0x09,"数据类型:octet-string,8位字节串",0),
    VISIBLE_STRING(String.class,(byte)0x0A,"数据类型:visible-string,ASCII字符串",0),
    INTEGER(Integer.class,(byte)0x0F,"数据类型:integer,8位整数",1),
    LONG_UNSIGNED(Integer.class,(byte)0x12,"数据类型：integer,16位整数",2),
    LONG64_UNSIGNED(Long.class,(byte)0x15,"数据类型:integer,8位整数",8),
    DATE_TIME_S(Date.class,(byte)0x1C,"数据类型:octet-string(SIZE(7))",7),
    TI(TimeUnit.class,(byte)0x54,"数据类型:时间间隔TI(Time Interval)",0);
    private final Class aClass;
    private final byte sign;
    private final String desc;
    private final int length;
    DataType(Class aClass,byte sign, String desc,int length) {
        this.aClass = aClass;
        this.sign = sign;
        this.desc = desc;
        this.length = length;
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
