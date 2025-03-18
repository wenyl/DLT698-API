package cn.com.wenyl.bs.config;

import cn.com.wenyl.bs.config.jwt.JwtConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Swimming Dragon
 * @description: 胚子swagger
 * @date 2023年12月04日 14:27
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        //添加请求参数，这里把token作为请求头参数传入后端
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterBuilder.name(JwtConstant.REQUEST_HEADER_TOKEN).description("token令牌").modelRef(new ModelRef("String")).parameterType("header")
                .required(false).build();
        parameterList.add(parameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())  // 设置API信息
                .select()  // 选择要暴露的API接口路径和请求方法
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())  // 选择要暴露的API接口路径和请求方法
                .build()
                .globalOperationParameters(parameterList);  // 构建Swagger文档对象，用于生成接口文档和UI界面
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()  // 创建API信息对象，用于设置API基本信息和描述等
                .title("black-shop")  // 设置标题
                .description("black-shop接口信息")  // 设置描述
                .version("1.0")  // 设置版本号
                .contact(new Contact("Swimming Dragon", "www.wenyoulong.com", "3424675994@qq.com"))  // 设置联系信息（可选）
                .build();  // 构建API信息对象，生成接口文档和UI界面时会显示这些信息
    }
}
