package cn.com.wenyl.bs.generator.service.impl;

import cn.com.wenyl.bs.generator.constants.FileDirectory;
import cn.com.wenyl.bs.generator.constants.FileName;
import cn.com.wenyl.bs.generator.constants.MySQLFieldTypeEnum;
import cn.com.wenyl.bs.generator.constants.TemplateFilePath;
import cn.com.wenyl.bs.generator.entity.TableColumnEntity;
import cn.com.wenyl.bs.generator.entity.TableEntity;
import cn.com.wenyl.bs.generator.mapper.CodeGeneratorMapper;
import cn.com.wenyl.bs.generator.service.ICodeGeneratorService;
import cn.com.wenyl.bs.exceptions.GeneratorCodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;

@Service(value = "codeGeneratorService")
@Slf4j
public class CodeGeneratorServiceImpl implements ICodeGeneratorService {
    @Resource
    private CodeGeneratorMapper codeGeneratorMapper;
    @Resource
    private VelocityEngine engine;

    @Override
    public void generatorCode(String filePath,String basePackage, String entityPackage, String databaseName, String tableName) throws GeneratorCodeException,IOException {
        TableEntity tableEntity = getTableEntity(databaseName, tableName);
        tableEntity.setBasePackage(basePackage);
        tableEntity.setEntityPackage(entityPackage);
        // 将包路径转化为controller请求路径
        String packagePath = entityPackage.replaceAll("\\.","/");
        tableEntity.setPackagePath(packagePath);
        String javaBaseDir = filePath + FileDirectory.JAVA_PATH+(basePackage+"."+entityPackage).replaceAll("\\.", Matcher.quoteReplacement(File.separator))+File.separator;
        String entityPath = javaBaseDir + FileDirectory.ENTITY_PATH + tableEntity.getClassName()+ FileName.ENTITY_END_WITH;
        String controllerPath = javaBaseDir + FileDirectory.CONTROLLER_PATH + tableEntity.getClassName()+FileName.CONTROLLER_END_WITH;
        String servicePath = javaBaseDir + FileDirectory.SERVICE_PATH + FileName.SERVICE_START_WITH+tableEntity.getClassName()+FileName.SERVICE_END_WITH;
        String serviceImplPath = javaBaseDir + FileDirectory.SERVICE_IMPL_PATH + tableEntity.getClassName()+FileName.SERVICE_IMPL_END_WITH;
        String MapperPath = javaBaseDir + FileDirectory.MAPPER_PATH + tableEntity.getClassName()+FileName.MAPPER_END_WITH;
        String MapperXmlPath = javaBaseDir + FileDirectory.MAPPER_XML_PATH + tableEntity.getClassName()+FileName.MAPPER_XML_END_WITH;
        VelocityContext velocityContext = createVelocityContext(tableEntity);
        // 生成后台代码
        generatorEntity(entityPath,velocityContext);
        generatorController(controllerPath,velocityContext);
        generatorService(servicePath,velocityContext);
        generatorServiceImpl(serviceImplPath,velocityContext);
        generatorMapper(MapperPath,velocityContext);
        generatorMapperXml(MapperXmlPath,velocityContext);

        // 生成前台代码
        String vueBaseDir = filePath+FileDirectory.VUE_PATH;
        String vueModulePath = entityPackage.replaceAll("\\.", Matcher.quoteReplacement(File.separator))+File.separator;
        String vuePagePath = vueBaseDir + FileDirectory.VUE_PAGE_PATH + vueModulePath+tableEntity.getClassName()+FileName.VUE_PAGE_END_WITH;
        String vueApiTsPath = vueBaseDir + FileDirectory.VUE_TS_PATH + vueModulePath+tableEntity.getClassAttrName()+FileName.VUE_API_END_WITH;
        String vueTypeTsPath = vueBaseDir + FileDirectory.VUE_TS_PATH + vueModulePath+tableEntity.getClassAttrName()+FileName.VUE_TYPE_END_WITH;

        generatorVuePage(vuePagePath,velocityContext);
        generatorTsApi(vueApiTsPath,velocityContext);
        generatorTsType(vueTypeTsPath,velocityContext);
    }



    /**
     * 创建VelocityContext实例，并添加数据到上下文中
     * @param tableEntity 表信息
     * @return VelocityContext实例
     */
    private VelocityContext createVelocityContext(TableEntity tableEntity){
        VelocityContext context = new VelocityContext();
        context.put("entity",tableEntity);
        return context;
    }

    /**
     * 生成java实体的代码
     * @param filePath 代码生成的文件全路径
     * @param context 模板渲染所需的变量信息
     * @throws IOException 创建、写入文件时IO异常
     */
    private void generatorEntity(String filePath,VelocityContext context)  throws IOException {
        Template template = engine.getTemplate(TemplateFilePath.ENTITY_TEMPLATE_PATH);
        // 将模板和数据合并生成最终的 Java 代码字符串
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        writeCodeToFile(writer,filePath);
    }
    /**
     * 将代码写入到文件
     * @param writer 代码信息
     * @param filePath 代码生成的文件全路径
     * @throws IOException 创建、写入文件时IO异常
     */
    private void writeCodeToFile(StringWriter writer, String filePath) throws IOException {
        File file = new File(filePath);
        File directory = file.getParentFile();
        if(!directory.exists()){
            boolean createDirectorySuccess = directory.mkdirs();
            if(!createDirectorySuccess){
                throw new IOException("文件目录创建异常");
            }

        }
        if(file.exists()){
            boolean delete = file.delete();
            if(!delete){
                throw new IOException("代码文件已存在，删除代码文件异常");
            }
        }
        boolean createFileSuccess = file.createNewFile();
        if(createFileSuccess){
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(writer.toString());
            fileWriter.close();
        }else {
            throw new IOException("文件创建异常");
        }
    }

    /**
     * 生成controller的代码
     * @param filePath 代码生成的文件全路径
     * @param context 模板渲染所需的变量信息
     * @throws IOException 创建、写入文件时IO异常
     */
    private void generatorController(String filePath, VelocityContext context) throws IOException {
        Template template = engine.getTemplate(TemplateFilePath.CONTROLLER_TEMPLATE_PATH);
        // 将模板和数据合并生成最终的 Java 代码字符串
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        writeCodeToFile(writer,filePath);
    }

    /**
     * 生成service的代码
     * @param filePath 代码生成的文件全路径
     * @param context 模板渲染所需的变量信息
     * @throws IOException 创建、写入文件时IO异常
     */
    private void generatorService(String filePath, VelocityContext context) throws IOException {
        Template template = engine.getTemplate(TemplateFilePath.SERVICE_TEMPLATE_PATH);
        // 将模板和数据合并生成最终的 Java 代码字符串
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        writeCodeToFile(writer,filePath);
    }
    /**
     * 生成service实现类的代码
     * @param filePath 代码生成的文件全路径
     * @param context 模板渲染所需的变量信息
     * @throws IOException 创建、写入文件时IO异常
     */
    private void generatorServiceImpl(String filePath,VelocityContext context) throws IOException {
        Template template = engine.getTemplate(TemplateFilePath.SERVICE_IMPL_TEMPLATE_PATH);
        // 将模板和数据合并生成最终的 Java 代码字符串
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        writeCodeToFile(writer,filePath);
    }

    /**
     * 生成mapper的代码
     * @param filePath 代码生成的文件全路径
     * @param context 模板渲染所需的变量信息
     * @throws IOException 创建、写入文件时IO异常
     */
    private void generatorMapper(String filePath, VelocityContext context) throws IOException {
        Template template = engine.getTemplate(TemplateFilePath.MAPPER_TEMPLATE_PATH);
        // 将模板和数据合并生成最终的 Java 代码字符串
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        writeCodeToFile(writer,filePath);
    }
    /**
     * 生成mapper对应的xml的代码
     * @param filePath 代码生成的文件全路径
     * @param context 模板渲染所需的变量信息
     * @throws IOException 创建、写入文件时IO异常
     */
    private void generatorMapperXml(String filePath, VelocityContext context) throws IOException {
        Template template = engine.getTemplate(TemplateFilePath.MAPPER_XML_TEMPLATE_PATH);
        // 将模板和数据合并生成最终的 Java 代码字符串
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        writeCodeToFile(writer,filePath);
    }


    /**
     * 生成vue列表页的代码
     * @param filePath 代码生成的文件全路径
     * @param context 模板渲染所需的变量信息
     * @throws IOException 创建、写入文件时IO异常
     */
    private void generatorVuePage(String filePath, VelocityContext context)  throws IOException {
        Template template = engine.getTemplate(TemplateFilePath.VUE_PAGE_TEMPLATE_PATH);
        // 将模板和数据合并生成最终的 Java 代码字符串
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        writeCodeToFile(writer,filePath);
    }
    /**
     * 生成vue列表页的代码
     * @param filePath 代码生成的文件全路径
     * @param context 模板渲染所需的变量信息
     * @throws IOException 创建、写入文件时IO异常
     */
    private void generatorTsApi(String filePath, VelocityContext context)   throws IOException {
        Template template = engine.getTemplate(TemplateFilePath.VUE_TS_API_TEMPLATE_PATH);
        // 将模板和数据合并生成最终的 Java 代码字符串
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        writeCodeToFile(writer,filePath);
    }
    /**
     * 生成vue列表页的代码
     * @param filePath 代码生成的文件全路径
     * @param context 模板渲染所需的变量信息
     * @throws IOException 创建、写入文件时IO异常
     */
    private void generatorTsType(String filePath, VelocityContext context)   throws IOException {
        Template template = engine.getTemplate(TemplateFilePath.VUE_TS_TYPE_TEMPLATE_PATH);
        // 将模板和数据合并生成最终的 Java 代码字符串
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        writeCodeToFile(writer,filePath);
    }

    @Override
    public TableEntity getTableEntity(String databaseName, String tableName) throws GeneratorCodeException {
        TableEntity tableEntity = codeGeneratorMapper.getTableInfo(databaseName,tableName);
        if(tableEntity==null || StringUtils.isEmpty(tableEntity.getTableName())){
            throw new GeneratorCodeException("数据库"+databaseName+"或表"+tableName+"不存在");
        }
        // 获取实体类名，首字母小写
        String entityClass = getJavaEntityName(tableName);
        tableEntity.setClassName(entityClass);
        // 获取实体类对应的变量名，首字母小写
        String entityAttrName = getJavaAttrName(tableName);
        tableEntity.setClassAttrName(entityAttrName);
        // 获取表字段信息
        List<TableColumnEntity> tableColumns = getTableColumns(databaseName, tableName);
        tableEntity.setColumns(tableColumns);
        return tableEntity;
    }

    @Override
    public List<TableColumnEntity>  getTableColumns(String databaseName, String tableName) throws GeneratorCodeException {
        List<TableColumnEntity> tableColumnInfo = codeGeneratorMapper.getTableColumnInfo(databaseName, tableName);
        Set<String> javaTypeClass = new HashSet<>();
        for(TableColumnEntity entity:tableColumnInfo){
            // 获取mysql字段类型对应的java字段类型
            String dbColumnType = entity.getDbColumnType();
            MySQLFieldTypeEnum mySQLFieldTypeEnum = MySQLFieldTypeEnum.getMySQLFieldTypeEnumBySqlFieldType(dbColumnType);
            if(mySQLFieldTypeEnum == null){
                String message = "找不到"+dbColumnType+"对应的Java类型,"+"可以在cn.com.wenyl.bs.code.generator.constants.MySQLFieldTypeEnum中添加该类型";
                log.error(message);
                throw new GeneratorCodeException(message);
            }
            // 设置java字段类型
            String javaType = mySQLFieldTypeEnum.getJavaType();
            entity.setJavaType(javaType);
            // 设置java字段类型所在的包路径,这里用set控制包路径，防止在模板生成的时候重复引入
            String javaTypeClassPath = mySQLFieldTypeEnum.getJavaTypeClassPath();
            if(!javaTypeClass.contains(javaTypeClassPath)){
                javaTypeClass.add(javaTypeClassPath);
                entity.setJavaTypePackage(javaTypeClassPath);
            }
            String dbColumnName = entity.getDbColumnName();
            // 设置java属性名
            String javaAttrName = getJavaAttrName(dbColumnName);
            entity.setJavaAttrName(javaAttrName);

        }
        return tableColumnInfo;
    }


    @Override
    public String getJavaAttrName(String dbColumnName){
        // 为空直接返回null
        if(StringUtils.isEmpty(dbColumnName)){
            return null;
        }
        // 根据下划线分割为数组
        String[] nameArray = dbColumnName.split("_");
        // 长度唯一，直接返回
        if(nameArray.length == 1){
            return dbColumnName;
        }
        // 长度不唯一，从第二个字符串开始，将首字母变为大写
        StringBuilder ret = new StringBuilder();
        ret.append(nameArray[0]);
        for(int i=1;i<nameArray.length;i++){
            String currentChar = nameArray[i];
            String newStr = currentChar.substring(0,1).toUpperCase()+currentChar.substring(1);
            ret.append(newStr);
        }
        return ret.toString();
    }

    @Override
    public String getJavaEntityName(String dbTableName) {
        // 为空直接返回null
        if(StringUtils.isEmpty(dbTableName)){
            return null;
        }

        // 根据下划线分割为数组
        String[] nameArray = dbTableName.split("_");
        // 长度唯一，直接返回
        if(nameArray.length == 1){
            return dbTableName;
        }
        // 从第一个字符串开始，每个字符串首字母变为大写
        StringBuilder ret = new StringBuilder();
        for (String currentChar : nameArray) {
            String newStr = currentChar.substring(0, 1).toUpperCase() + currentChar.substring(1);
            ret.append(newStr);
        }
        return ret.toString();
    }
}

