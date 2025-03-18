package cn.com.wenyl.bs.generator.entity;

import lombok.Data;

/**
 * @author Swimming Dragon
 * @description: 表字段对应的实体属性信息
 * @date 2023年12月04日 9:28
 */
@Data
public class TableColumnEntity {
    // 字段对应的java类型
    private String javaType;
    // 字段对应的java包类路径
    private String javaTypePackage;
    // 字段对应的java属性名称
    private String javaAttrName;
    // 字段对应的数据库类型
    private String dbColumnType;
    // 字段名称
    private String dbColumnName;
    // 字段注释
    private String dbColumnComment;
    // 是否主键
    private Boolean isPrimaryKey;
}
