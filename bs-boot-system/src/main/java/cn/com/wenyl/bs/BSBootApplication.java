package cn.com.wenyl.bs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan(value={"cn.com.wenyl.bs.**.mapper*"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BSBootApplication {
    public static void main(String[] args){
        SpringApplication.run(BSBootApplication.class);
    }
}
