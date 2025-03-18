package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

@Getter
public enum DataType {
    _09((byte)0x09,"数据类型：oct-string");
    private final byte sign;
    private final String desc;
    DataType(byte sign, String desc){
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
