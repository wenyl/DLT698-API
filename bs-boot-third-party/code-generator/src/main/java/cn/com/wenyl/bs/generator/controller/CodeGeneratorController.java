package cn.com.wenyl.bs.generator.controller;

import cn.com.wenyl.bs.exceptions.GeneratorCodeException;
import cn.com.wenyl.bs.generator.service.ICodeGeneratorService;
import cn.com.wenyl.bs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Swimming Dragon
 * @description: 代码生成接口
 * @date 2023年12月04日 16:10
 */
@RestController
@RequestMapping("/codeGenerator")
@Api(tags="代码生成")
public class CodeGeneratorController {
    @Resource(name = "codeGeneratorService")
    private ICodeGeneratorService codeGeneratorService;


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
    @GetMapping("/generatorCode")
    @ApiOperation(value="代码生成-代码生成", notes="代码生成-代码生成")
    public R<?> generatorCode(@RequestParam("filePath") String filePath,@RequestParam("basePackage") String basePackage,
                              @RequestParam("entityPackage") String entityPackage, @RequestParam("databaseName") String databaseName,
                              @RequestParam("tableName") String tableName) throws GeneratorCodeException, IOException{
        codeGeneratorService.generatorCode(filePath,basePackage,entityPackage,databaseName,tableName);
        return R.ok("成功");
    }
}
