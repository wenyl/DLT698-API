package cn.com.wenyl.bs.generator.entity;

import lombok.Data;

import java.util.List;


/**
 * @author Swimming Dragon
 * @description: 表对应的java实体信息
 * @date 2023年12月04日 9:28
 */
@Data
public class TableEntity {
    // 基础包路径，默认为cn.com.wenyl.bs
    private String basePackage = "cn.com.wenyl.bs";
    // 代码包路径，与basePackage拼接作为完整代码包路径
    private String entityPackage;
    // 代码包路径，将.转化为/，用于controller路径
    private String packagePath;
    // 表名
    private String tableName;
    // 表注釋
    private String tableComment;
    // 实体名，首字母大写
    private String className;
    // 实体名，首字母小写
    private String classAttrName;
    // 列信息
    private List<TableColumnEntity> columns;

}
