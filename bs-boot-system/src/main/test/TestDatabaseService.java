import cn.com.wenyl.bs.BSBootApplication;
import cn.com.wenyl.bs.generator.entity.TableColumnEntity;
import cn.com.wenyl.bs.generator.entity.TableEntity;
import cn.com.wenyl.bs.generator.service.ICodeGeneratorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BSBootApplication.class)
public class TestDatabaseService {
    @Resource(name = "codeGeneratorService")
    private ICodeGeneratorService codeGeneratorService;

    @Test
    public void testDatabase() throws Exception{
        codeGeneratorService.generatorCode("D:"+ File.separator,"cn.com.wenyl.bs","mall","mall", "mall_order");
    }
}
