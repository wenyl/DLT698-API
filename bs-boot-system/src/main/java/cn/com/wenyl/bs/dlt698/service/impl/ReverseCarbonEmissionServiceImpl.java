package cn.com.wenyl.bs.dlt698.service.impl;

import com.alibaba.fastjson.JSON;
import cn.com.wenyl.bs.dlt698.annotation.DeviceOperateContext;
import cn.com.wenyl.bs.dlt698.annotation.DeviceOperateLog;
import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.entity.GetRequestNormalData;
import cn.com.wenyl.bs.dlt698.entity.GetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.entity.GetResponseNormalFrame;
import cn.com.wenyl.bs.dlt698.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.service.RS485Service;
import cn.com.wenyl.bs.dlt698.service.ReverseCarbonEmissionService;
import cn.com.wenyl.bs.dlt698.utils.BCDUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import cn.com.wenyl.bs.dlt698.utils.SerialCommUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class ReverseCarbonEmissionServiceImpl implements ReverseCarbonEmissionService {

    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private RS485Service rs485Service;
    @Override
    @DeviceOperateLog(jobName = "反向碳排放管理-昨日累计",valueSign = "rce",valueLabel = "反向碳排放",hasValue = true)
    public Object yesterdayCarbonAccumulate(String carbonDeviceAddress)  throws ExecutionException, InterruptedException, TimeoutException {

        // 查询昨日碳排放累计量
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = (GetRequestNormalFrame)builder.getFrame(FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO, BCDUtils.encodeBCD(carbonDeviceAddress),
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO,OI.REVERSE_CARBON_EMISSION, AttrNum.ATTR_02,AttributeIndex.ZERO,TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        try{
            byte[] returnFrame = rs485Service.sendByte(bytes);
            log.info("收到数据帧{}", HexUtils.bytesToHex(returnFrame));
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
}
