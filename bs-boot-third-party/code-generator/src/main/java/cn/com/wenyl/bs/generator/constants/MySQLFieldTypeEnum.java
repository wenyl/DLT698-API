package cn.com.wenyl.bs.generator.constants;

import lombok.Getter;


/**
 * @author Swimming Dragon
 * @description: mysql常见字段类型对应的java字段类型
 * @date 2023年12月04日 9:28
 */
public enum MySQLFieldTypeEnum {
    VARCHAR("varchar","String","java.lang.String"),
    TEXT("text","String","java.lang.String"),
    LONGTEXT("longtext","String","java.lang.String"),

    INT("int","Integer","java.lang.Integer"),
    TINYINT("tinyint","Short","java.lang.Short"),
    FLOAT("float","Float","java.lang.Float"),
    DOUBLE("double","Double","java.lang.Double"),
    DECIMAL("decimal","Double","java.lang.Double"),

    BLOB("blob","byte[]",null),

    DATETIME("datetime","Date","java.util.Date"),
    DATE("date","Date","java.util.Date");
    /*
    * sql字段类型
    * */
    final private String sqlType;
    /*
     * sql字段类型对应的java类型
     * */
    @Getter
    final private String javaType;
    /*
     * sql字段类型对应的java类型所在的包路径，用于import
     * */
    @Getter
    final private String javaTypeClassPath;

    MySQLFieldTypeEnum(String sqlType, String javaType, String javaTypeClassPath){
        this.sqlType = sqlType;
        this.javaType = javaType;
        this.javaTypeClassPath = javaTypeClassPath;
    }

    /**
     * 根据mysql的字段类型获取它对应的java实体字段类型和它所在的包路径
     * @param sqlType 字段对应的MySQL数据类型
     * @return 返回字段类型信息
     */
    public static MySQLFieldTypeEnum getMySQLFieldTypeEnumBySqlFieldType(String sqlType){
        MySQLFieldTypeEnum[] values = MySQLFieldTypeEnum.values();
        for(MySQLFieldTypeEnum mySQLFieldTypeEnum:values) {
            if (mySQLFieldTypeEnum.sqlType.equals(sqlType)) {
                return mySQLFieldTypeEnum;
            }
        }
        return null;
    }
}
