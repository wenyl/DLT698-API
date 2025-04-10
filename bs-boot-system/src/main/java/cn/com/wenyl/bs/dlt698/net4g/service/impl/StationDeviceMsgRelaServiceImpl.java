package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.net4g.entity.DeviceMsgHis;
import cn.com.wenyl.bs.dlt698.net4g.entity.StationDeviceMsgRela;
import cn.com.wenyl.bs.dlt698.net4g.entity.dto.DeviceMsgHisRela;
import cn.com.wenyl.bs.dlt698.net4g.mapper.StationDeviceMsgRelaMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceMsgHisService;
import cn.com.wenyl.bs.dlt698.net4g.service.StationDeviceMsgRelaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 设备消息历史关系 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Service
public class StationDeviceMsgRelaServiceImpl extends ServiceImpl<StationDeviceMsgRelaMapper, StationDeviceMsgRela> implements StationDeviceMsgRelaService {

    @Resource
    @Lazy
    private DeviceMsgHisService deviceMsgHisService;
    @Override
    public void saveRelaMsg(DeviceMsgHisRela msgHisRela) {
        StationDeviceMsgRela stationDeviceMsgRela = new StationDeviceMsgRela();
        LambdaQueryWrapper<DeviceMsgHis> hisQuery = Wrappers.lambdaQuery();
        hisQuery.eq(DeviceMsgHis::getDeviceId,msgHisRela.getDeviceId());
        hisQuery.eq(DeviceMsgHis::getDir,msgHisRela.getTargetDir());
        hisQuery.eq(DeviceMsgHis::getPrm,msgHisRela.getTargetPrm());
        hisQuery.eq(DeviceMsgHis::getApdu,msgHisRela.getTargetApdu());
        hisQuery.eq(DeviceMsgHis::getApduOpera,msgHisRela.getTargetApduOpera());
        hisQuery.eq(DeviceMsgHis::getOi,msgHisRela.getOi());
        hisQuery.eq(DeviceMsgHis::getAttrNum,msgHisRela.getAttrNum());
        hisQuery.eq(DeviceMsgHis::getAttrIndex,msgHisRela.getAttrIndex());
        hisQuery.orderByDesc(DeviceMsgHis::getCreateTime).last("LIMIT 1");

        DeviceMsgHis targetMsg = deviceMsgHisService.getOne(hisQuery);

        // 还没有目标数据，说明发起的请求
        if(targetMsg == null){
            stationDeviceMsgRela.setDeviceId(msgHisRela.getDeviceId());
            stationDeviceMsgRela.setDir(msgHisRela.getDir());
            // 这是碳表发起的上报
            if(msgHisRela.getDir() == 1 && msgHisRela.getPrm() == 0){
                stationDeviceMsgRela.setDir(1);
                stationDeviceMsgRela.setDeviceMsgId(msgHisRela.getId());
            }
            // 这是主站发起的请求
            if(msgHisRela.getDir() == 0 && msgHisRela.getPrm() == 1){
                stationDeviceMsgRela.setDir(0);
                stationDeviceMsgRela.setMainStationMsgId(msgHisRela.getId());
            }
            this.save(stationDeviceMsgRela);
        }else{
            // 目标数据已经存在则要去关系表中查出来
            LambdaQueryWrapper<StationDeviceMsgRela> relaQuery = Wrappers.lambdaQuery();
            relaQuery.eq(StationDeviceMsgRela::getDeviceId,msgHisRela.getDeviceId());
            // 目标数据是由碳表发起的上报，则这条消息应该是主站对碳表上报的回复
            if(targetMsg.getDir() == 1 && targetMsg.getPrm() == 0 && msgHisRela.getDir() == 0 && msgHisRela.getPrm() == 0){
                relaQuery.eq(StationDeviceMsgRela::getDeviceMsgId,targetMsg.getId());
                StationDeviceMsgRela one = this.getOne(relaQuery);
                one.setMainStationMsgId(msgHisRela.getId());
                this.save(one);
            }
            // 目标数据是主站发起的请求，则这条消息是碳表对主站的回复
            if(targetMsg.getDir() == 0 && targetMsg.getPrm() == 1 && msgHisRela.getDir() == 1 && msgHisRela.getPrm() == 1){
                relaQuery.eq(StationDeviceMsgRela::getMainStationMsgId,targetMsg.getId());
                StationDeviceMsgRela one = this.getOne(relaQuery);
                one.setDeviceMsgId(msgHisRela.getId());
                this.save(one);
            }
        }

    }
}
