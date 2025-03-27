package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.entity.dto.CarbonDeviceDataDto;
import com.alibaba.fastjson.JSON;
import cn.com.wenyl.bs.dlt698.annotation.DeviceOperateContext;
import cn.com.wenyl.bs.dlt698.annotation.DeviceOperateLog;
import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.entity.*;
import cn.com.wenyl.bs.dlt698.service.*;
import cn.com.wenyl.bs.dlt698.utils.BCDUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import cn.com.wenyl.bs.dlt698.utils.SerialCommUtils;
import com.alibaba.fastjson2.JSONException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
@Slf4j
@Service("carbonDeviceService")
public class CarbonDeviceServiceImpl implements CarbonDeviceService {
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private RS485Service rs485Service;
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
    @DeviceOperateLog(jobName = "碳表管理-获取碳表地址",valueSign = "carbonDeviceAddress",valueLabel = "碳表地址",hasValue = true)
    public Object getCarbonDeviceAddress() throws RuntimeException,TimeoutException, ExecutionException, InterruptedException {

        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = (GetRequestNormalFrame)builder.getFrame(FunctionCode.THREE,ScramblingCodeFlag.NOT_SCRAMBLING_CODE,FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST,AddressType.DISTRIBUTION_ADDRESS,LogicAddress.ZERO,Address.DISTRIBUTION_ADDRESS,
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO,OI.MAIL_ADDRESS, AttrNum.ATTR_02,AttributeIndex.ZERO,TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        try{
            byte[] returnFrame = rs485Service.sendByte(bytes);
            GetResponseNormalFrameParser parser = (GetResponseNormalFrameParser)frameParseProcessor.getFrameParser(GetResponseNormalFrame.class);
            Object obj = parser.getData(parser.parseFrame(returnFrame));
            DeviceOperateContext.get().setSentFrame(HexUtils.bytesToHex(bytes));
            DeviceOperateContext.get().setReceivedFrame(HexUtils.bytesToHex(returnFrame));
            DeviceOperateContext.get().setValueJson(JSON.toJSONString(obj));
            return obj;
        } finally{
            SerialCommUtils.getInstance().closePort();
        }
    }
    @Override
    @DeviceOperateLog(jobName = "碳表管理-链接碳表",valueSign = "connectInfo",valueLabel = "链接信息",hasValue = true)
    public Object connectCarbonDevice(String carbonDeviceAddress) throws RuntimeException,TimeoutException, ExecutionException, InterruptedException  {
        ConnectRequestFrameBuilder builder = (ConnectRequestFrameBuilder)frameBuildProcessor.getFrameBuilder(ConnectRequestFrame.class);
        ConnectRequestFrame connectRequestFrame = (ConnectRequestFrame)builder.getFrame(FunctionCode.THREE,ScramblingCodeFlag.NOT_SCRAMBLING_CODE,FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST,AddressType.SINGLE_ADDRESS,LogicAddress.ZERO, BCDUtils.encodeBCD(carbonDeviceAddress),Address.CLIENT_ADDRESS);
        // 这里使用了默认的参数，有其他需要自己设置对应值即可
        ConnectRequestData requestData = new ConnectRequestData(PIID.ZERO_ZERO,TimeTag.NO_TIME_TAG);
        connectRequestFrame.setConnectRequestData(requestData);
        byte[] bytes = builder.buildFrame(connectRequestFrame);
        try{
            byte[] returnFrame = rs485Service.sendByte(bytes);
            ConnectResponseFrameParser parser = (ConnectResponseFrameParser)frameParseProcessor.getFrameParser(ConnectResponseFrame.class);
            Object obj = parser.getData(parser.parseFrame(returnFrame));
            DeviceOperateContext.get().setSentFrame(HexUtils.bytesToHex(bytes));
            DeviceOperateContext.get().setReceivedFrame(HexUtils.bytesToHex(returnFrame));
            DeviceOperateContext.get().setValueJson(JSON.toJSONString(obj));
            return obj;
        } finally{
            SerialCommUtils.getInstance().closePort();
        }
    }


    @Override
    @DeviceOperateLog(jobName = "碳表管理-获取数据(电流、电压、正向有功电能量、反向有功电能量、正向碳排放管理-昨日累计、反向碳排放管理-昨日累计)",valueSign = "getData",valueLabel = "数据",hasValue = false,screenData=true)
    public Object getData(String carbonDeviceAddress)  throws ExecutionException, InterruptedException, TimeoutException, JSONException{
        CarbonDeviceDataDto dataDto = new CarbonDeviceDataDto();
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
