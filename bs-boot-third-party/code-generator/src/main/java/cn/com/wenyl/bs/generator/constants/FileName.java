package cn.com.wenyl.bs.generator.constants;

/**
 * @author Swimming Dragon
 * @description: 定义生成代码的类名的开始结束符号,有需要可自行团价，用于{@link cn.com.wenyl.bs.generator.service.impl.CodeGeneratorServiceImpl}中的generatorCode方法设置文件名
 * @date 2023年12月04日 10:35
 */
public class FileName {
    public static final String ENTITY_END_WITH = ".java";
    public static final String CONTROLLER_END_WITH = "Controller.java";
    public static final String SERVICE_START_WITH = "I";
    public static final String SERVICE_END_WITH = "Service.java";
    public static final String SERVICE_IMPL_END_WITH = "ServiceImpl.java";
    public static final String MAPPER_END_WITH = "Mapper.java";
    public static final String MAPPER_XML_END_WITH = "Mapper.xml";
    public static final String VUE_PAGE_END_WITH = "List.vue";
    public static final String VUE_API_END_WITH = "Api.ts";
    public static final String VUE_TYPE_END_WITH = "Type.ts";
}
