package cn.com.wenyl.bs.generator.mapper;


import cn.com.wenyl.bs.exceptions.GeneratorCodeException;
import cn.com.wenyl.bs.generator.entity.TableColumnEntity;
import cn.com.wenyl.bs.generator.entity.TableEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Swimming Dragon
 * @description: 查询数据库表和字段的元信息接口
 * @date 2023年12月04日 9:28
 */
public interface CodeGeneratorMapper{
    /**
     * 获取数据库表字段名、字段类型、字段注释、主键的isPrimary会被标识为true，其余为false {@link TableColumnEntity}
     * @param databaseName 数据库名称
     * @param tableName 表名称
     * @return 数据库表字段名、字段类型、字段注释信息 {@link TableEntity}
     */
    List<TableColumnEntity> getTableColumnInfo(@Param("databaseName") String databaseName,
                                                @Param("tableName") String tableName);
    /**
     * 获取数据库表名和表注释 {@link TableColumnEntity}
     * @param databaseName 数据库名称
     * @param tableName 表名称
     * @return 数据库表名和表注释 {@link TableEntity}
     */
    TableEntity getTableInfo(@Param("databaseName") String databaseName,
                             @Param("tableName") String tableName);
}
