import cn.com.wenyl.bs.BSBootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Swimming Dragon
 * @description: 测试redis
 * @date 2023年12月04日 21:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BSBootApplication.class)
public class TestRedis {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Test
    public void testDatabase() throws Exception{
        redisTemplate.opsForValue().set("long","6666");
        System.out.println(redisTemplate.opsForValue().get("long"));
    }
}
