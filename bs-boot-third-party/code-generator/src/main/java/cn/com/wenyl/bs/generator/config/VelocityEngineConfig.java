package cn.com.wenyl.bs.generator.config;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Swimming Dragon
 * @description: 配置VelocityEngine引擎
 * @date 2023年12月04日 9:28
 */
@Configuration
public class VelocityEngineConfig {
    @Bean
    public VelocityEngine init(){
        // 初始化 VelocityEngine 实例
        VelocityEngine engine = new VelocityEngine();
        // 从classpath加载模板
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        // 指定文件读取写入的编码
        engine.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
        engine.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
        engine.init();
        return engine;
    }
}
