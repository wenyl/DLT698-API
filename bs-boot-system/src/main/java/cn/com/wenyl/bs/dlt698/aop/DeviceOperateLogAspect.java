package cn.com.wenyl.bs.dlt698.aop;

import cn.com.wenyl.bs.dlt698.service.CarbonDeviceService;
import com.alibaba.fastjson.JSON;
import cn.com.wenyl.bs.dlt698.annotation.DeviceOperateContext;
import cn.com.wenyl.bs.dlt698.annotation.DeviceOperateLog;
import cn.com.wenyl.bs.dlt698.entity.CarbonDeviceMessage;
import cn.com.wenyl.bs.dlt698.entity.CarbonDeviceTask;
import cn.com.wenyl.bs.dlt698.service.CarbonDeviceMessageService;
import cn.com.wenyl.bs.dlt698.service.CarbonDeviceTaskService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class DeviceOperateLogAspect {
    @Resource
    private CarbonDeviceTaskService carbonDeviceTaskService;
    @Resource
    private CarbonDeviceMessageService carbonDeviceMessageService;
    @Around("@annotation(cn.com.wenyl.bs.dlt698.annotation.DeviceOperateLog)")
    public Object logCommunication(ProceedingJoinPoint joinPoint) throws Throwable {
        DeviceOperateContext context = DeviceOperateContext.get();
        Long jobId = context.getJobId();
        // 如果 `jobId` 为空，说明是外层主方法
        boolean isRootMethod = (jobId == null);
        if (isRootMethod) {
            context.setRootMethod(true);
        }

        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DeviceOperateLog annotation = method.getAnnotation(DeviceOperateLog.class);
        String jobName = annotation.jobName();
        // **如果是主方法，创建任务**
        CarbonDeviceTask mainTask = null;
        if (isRootMethod) {
            mainTask = new CarbonDeviceTask();
            mainTask.setStartTime(LocalDateTime.now());
            mainTask.setEndTime(LocalDateTime.now());
            mainTask.setJobName(jobName);
            if(annotation.screenData()){
                mainTask.setScreenData(true);
            }
            carbonDeviceTaskService.save(mainTask);
            jobId = mainTask.getId();
            context.setJobId(jobId);
        }

        // 返回结果
        Object result = null;
        try {
            result = joinPoint.proceed();
            // **存储返回值**
            if (result != null) {
                context.setValueJson(JSON.toJSONString(result));
            }

            return result;
        } finally {
            if (jobId != null) {
                if (annotation.hasValue()) {
                    CarbonDeviceMessage message = new CarbonDeviceMessage();
                    message.setJobId(jobId);
                    message.setOperateName(jobName);
                    message.setSendByteStr(context.getSentFrame());
                    message.setReceiveByteStr(context.getReceivedFrame());
                    message.setValueSign(annotation.valueSign());
                    message.setValueLabel(annotation.valueLabel());
                    message.setValueJson(JSON.toJSONString(result));
                    carbonDeviceMessageService.save(message);
                }
            }
            if(isRootMethod){
                mainTask.setEndTime(LocalDateTime.now());
                carbonDeviceTaskService.updateById(mainTask);
                DeviceOperateContext.clear(); // 清理上下文
            }
        }
    }
}
