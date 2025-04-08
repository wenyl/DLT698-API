package cn.com.wenyl.bs.dlt698.client.service.impl;

import cn.com.wenyl.bs.dlt698.client.service.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.com.wenyl.bs.dlt698.client.annotation.CarbonDeviceAddress;
import cn.com.wenyl.bs.dlt698.client.annotation.DeviceOperateLog;
import cn.com.wenyl.bs.dlt698.common.entity.CarbonDeviceTask;
import cn.com.wenyl.bs.dlt698.common.entity.dto.CarbonDeviceTaskDto;
import cn.com.wenyl.bs.dlt698.common.entity.dto.CarbonDeviceTaskMsgDto;
import cn.com.wenyl.bs.dlt698.client.mapper.CarbonDeviceTaskMapper;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * <p>
 * 碳表任务 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2025-03-26
 */
@Service
public class CarbonDeviceTaskServiceImpl extends ServiceImpl<CarbonDeviceTaskMapper, CarbonDeviceTask> implements CarbonDeviceTaskService {
    @Resource
    private CarbonDeviceTaskMapper carbonDeviceTaskMapper;
    @Resource
    private ElectricCurrentService electricCurrentService;
    @Resource
    private VoltageService voltageService;
    @Resource
    private ForwardCarbonEmissionService forwardCarbonEmissionService;
    @Resource
    private ReverseCarbonEmissionService reverseCarbonEmissionService;
    @Resource
    private PAEEnergyService paeEnergyService;
    @Resource
    private RAEEnergyService raeEnergyService;
    @Override
    public IPage<CarbonDeviceTaskMsgDto> pageScreenData(int currentPage, int pageSize,String carbonDeviceAddress) {
        Page<CarbonDeviceTaskDto> page = new Page<>(currentPage, pageSize);
        IPage<CarbonDeviceTaskDto> data = carbonDeviceTaskMapper.pageScreenData(page,carbonDeviceAddress);
        // 手动转换数据
        List<CarbonDeviceTaskMsgDto> retData = data.getRecords().stream()
                .map(user -> {
                    CarbonDeviceTaskMsgDto dto = new CarbonDeviceTaskMsgDto();
                    dto.setId(user.getJobId());
                    dto.setEndTime(user.getEndTime());
                    String message = user.getMessages();
                    JSONArray array = JSONArray.parseArray(message);
                    for (int i = 0; i < array.size(); i++) {
                        // 获取单个对象
                        JSONObject jsonObject = array.getJSONObject(i);
                        // 获取字段值
                        String valueJson = jsonObject.getString("value_json");
                        String valueSign = jsonObject.getString("value_sign");
                        switch (valueSign) {
                            case "raee":
                                dto.setRaee(JSON.parse(valueJson));
                                continue;
                            case "paee":
                                dto.setPaee(JSON.parse(valueJson));
                                continue;
                            case "rce":
                                dto.setRce(JSON.parse(valueJson));
                                continue;
                            case "fce":
                                dto.setFce(JSON.parse(valueJson));
                                continue;
                            case "voltage":
                                dto.setVoltage(JSON.parse(valueJson));
                                continue;
                            case "electricCurrent":
                                dto.setElectricCurrent(JSON.parse(valueJson));
                                break;
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        // 将转换后的数据设置回 Page 对象
        IPage<CarbonDeviceTaskMsgDto> ret = new Page<>();
        ret.setRecords(retData);
        ret.setTotal(data.getTotal()); // 保留分页信息
        ret.setSize(data.getSize());
        ret.setCurrent(data.getCurrent());
        return ret;
    }

    @Override
    @DeviceOperateLog(jobName = "碳表管理-获取数据(电流、电压、正向有功电能量、反向有功电能量、正向碳排放管理-昨日累计、反向碳排放管理-昨日累计)",valueSign = "getData",valueLabel = "数据",hasValue = false,screenData=true)
    public Object getData(@CarbonDeviceAddress String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CarbonDeviceTaskMsgDto dataDto = new CarbonDeviceTaskMsgDto();
        Object electricCurrent = electricCurrentService.getElectricCurrent(carbonDeviceAddress);
        Object voltage = voltageService.getVoltage(carbonDeviceAddress);
        Object fce = forwardCarbonEmissionService.yesterdayCarbonAccumulate(carbonDeviceAddress);
        Object rce = reverseCarbonEmissionService.yesterdayCarbonAccumulate(carbonDeviceAddress);
        Object paee = paeEnergyService.getPAEEnergy(carbonDeviceAddress);
        Object raee = raeEnergyService.getRAEEnergy(carbonDeviceAddress);
        dataDto.setElectricCurrent(electricCurrent);
        dataDto.setVoltage(voltage);
        dataDto.setFce(fce);
        dataDto.setRce(rce);
        dataDto.setPaee(paee);
        dataDto.setRaee(raee);
        return dataDto;
    }
}
