package cn.com.wenyl.bs.dlt698.client.task;

import cn.com.wenyl.bs.dlt698.client.service.CarbonDeviceTaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
public class MyScheduledTask {
    @Resource
    private CarbonDeviceTaskService carbonDeviceTaskService;

    @Scheduled(cron = "0 0/15 * * * ?")
    public void getData() throws ExecutionException, InterruptedException, TimeoutException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        carbonDeviceTaskService.getData("012503200001");
    }
}
