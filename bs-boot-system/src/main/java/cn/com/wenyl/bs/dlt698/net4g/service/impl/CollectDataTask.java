package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.net4g.service.*;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class CollectDataTask {
    @Resource
    private ElectricService electricService;
    @Resource
    private VoltageService voltageService;
    @Resource
    private ForwardCarbonEmissionService forwardCarbonEmissionService;
    @Resource
    private ReverseCarbonEmissionService reverseCarbonEmissionService;

    @Resource
    private ForwardActivePowerService forwardActivePowerService;
    @Resource
    private ReverseActivePowerService reverseActivePowerService;
    @Resource
    private DeviceChannelManager deviceChannelManager;
    @Scheduled(cron = "0 0 1 * * ?")
    public void collectDataTask() throws Exception {
        Set<String> deviceIps = deviceChannelManager.getIps();
        for(String ip:deviceIps){
            electricService.getElectricCurrent(ip);
            voltageService.getVoltage(ip);
            forwardCarbonEmissionService.getForwardCarbonEmission(ip);
            reverseCarbonEmissionService.getReverseCarbonEmission(ip);
            forwardActivePowerService.getForwardActivePower(ip);
            reverseActivePowerService.getReverseActivePower(ip);
        }
    }
}
