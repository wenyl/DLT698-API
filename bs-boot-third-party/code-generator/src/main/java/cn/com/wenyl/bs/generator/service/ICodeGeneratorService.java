package cn.com.wenyl.bs.generator.service;

import cn.com.wenyl.bs.generator.entity.TableColumnEntity;
import cn.com.wenyl.bs.generator.entity.TableEntity;
import cn.com.wenyl.bs.exceptions.GeneratorCodeException;

import java.io.IOException;
import java.util.List;

/**
 * 代码生成服务
 */
public interface ICodeGeneratorService {

    /**
     * 生成代码，包含entity、controller、service、mapper
     * 代码生成实际路径为filePath+basePackage+entityPackage  eg:D://+cn/com/wenyl/bs+/+mall
     * 其中D://为filePath,cn/com/wenyl/bs是basePackage将.转化成了/，entityPackage
     * @param filePath 代码生成得目录
     * @param basePackage 基础包路径
     * @param entityPackage 实体包路径
     * @param databaseName 数据库名称
     * @param tableName 表名称
     * @throws GeneratorCodeException 自定义代码生成异常 {@link GeneratorCodeException}
     * @throws IOException IO异常，捕获将代码写入到文件中发生异常的情况
     */
    void generatorCode(String filePath,String basePackage,String entityPackage,String databaseName,String tableName) throws GeneratorCodeException, IOException;

    /**
     * 获取数据库表对应的java代码的映射信息，包含包路径，实体类名，表名，列信息等信息，详情见 {@link TableEntity}
     * @param databaseName 数据库名称
     * @param tableName 表名称
     * @return 数据库表对应的java代码的映射信息 {@link TableEntity}
     * @throws GeneratorCodeException 自定义代码生成异常 {@link GeneratorCodeException}
     */
    TableEntity getTableEntity(String databaseName, String tableName) throws GeneratorCodeException;

    /**
     * 获取数据库表的列对应的java代码的映射信息 {@link TableColumnEntity}
     * @param databaseName 数据库名称
     * @param tableName 表名称
     * @return 数据库表的列对应的java代码的映射信息 {@link TableEntity}
     * @throws GeneratorCodeException 自定义代码生成异常 {@link GeneratorCodeException}
     */
    List<TableColumnEntity> getTableColumns(String databaseName, String tableName)  throws GeneratorCodeException;

    /**
     * 获取数据库列对应的java属性名，以_为标识，将列名分割为数组，遍历数组，除了第一个字符串外，其余字符串将首字母转化为大写,再拼接后返回
     * @param dbColumnName 列名
     * @return 将列名转换为对应的java属性名
     */
    String getJavaAttrName(String dbColumnName);

    /**
     * 获取数据库表对应的java实体名称，以_为标识，将列名分割为数组，遍历数组，将首字母都大写后拼接最后加上Entity后返回
     * @param dbTableName 数据库表名
     * @return 数据库表对应的java类名
     */
    String getJavaEntityName(String dbTableName);

}
