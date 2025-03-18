package cn.com.wenyl.bs.generator.constants;

import cn.com.wenyl.bs.exceptions.GeneratorCodeException;

import java.io.File;

/**
 * @author Swimming Dragon
 * @description: 指定代码生成目录的结构，按照controller、service、entity、mapper对生成的代码分目录存储，不要随意改动这里的File.separator,如果要自定义目目录结构，需要结合{@link cn.com.wenyl.bs.generator.service.impl.CodeGeneratorServiceImpl}中的generatorCode方法的路径设置
 * @date 2023年12月04日 10:18
 */
public class FileDirectory {
    public static final String JAVA_PATH = "java"+File.separator;
    public static final String ENTITY_PATH = "entity"+ File.separator;
    public static final String CONTROLLER_PATH = "controller"+ File.separator;
    public static final String SERVICE_PATH = "service"+ File.separator;
    public static final String SERVICE_IMPL_PATH = "service"+File.separator+"impl"+ File.separator;
    public static final String MAPPER_PATH = "mapper"+ File.separator;
    public static final String MAPPER_XML_PATH = "mapper"+File.separator+"xml"+ File.separator;

    public static final String VUE_PATH = "vue"+File.separator;
    public static final String VUE_PAGE_PATH = "views"+File.separator;
    public static final String VUE_TS_PATH = "api"+File.separator;
}
