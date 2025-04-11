package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.net4g.entity.DeviceMsgHis;
import cn.com.wenyl.bs.dlt698.net4g.entity.StationDeviceMsgRela;
import cn.com.wenyl.bs.dlt698.net4g.entity.dto.DeviceMsgHisRela;
import cn.com.wenyl.bs.dlt698.net4g.mapper.StationDeviceMsgRelaMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceMsgHisService;
import cn.com.wenyl.bs.dlt698.net4g.service.StationDeviceMsgRelaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
        try{
            StationDeviceMsgRela stationDeviceMsgRela = new StationDeviceMsgRela();
            LambdaQueryWrapper<DeviceMsgHis> hisQuery = Wrappers.lambdaQuery();
            hisQuery.eq(DeviceMsgHis::getDeviceId,msgHisRela.getDeviceId());
            hisQuery.eq(DeviceMsgHis::getDir,msgHisRela.getTargetDir());
            hisQuery.eq(DeviceMsgHis::getPrm,msgHisRela.getTargetPrm());
            hisQuery.eq(DeviceMsgHis::getApdu,msgHisRela.getTargetApdu());
            if(StringUtils.isNotBlank(msgHisRela.getTargetApduOpera())){
                hisQuery.eq(DeviceMsgHis::getApduOpera,msgHisRela.getTargetApduOpera());
            }
            if(StringUtils.isNotBlank(msgHisRela.getOi())){
                hisQuery.eq(DeviceMsgHis::getOi,msgHisRela.getOi());
            }
            if(StringUtils.isNotBlank(msgHisRela.getAttrNum())){
                hisQuery.eq(DeviceMsgHis::getAttrNum,msgHisRela.getAttrNum());
            }
            if(StringUtils.isNotBlank(msgHisRela.getAttrIndex())){
                hisQuery.eq(DeviceMsgHis::getAttrIndex,msgHisRela.getAttrIndex());
            }
            hisQuery.orderByDesc(DeviceMsgHis::getCreateTime).last("LIMIT 1");
            // 查询目标数据是否存在，有可能查到上一条消息对应的回复，所以当他存在时，还需要再去查询自己是否已经在rela中了
            DeviceMsgHis targetMsg = deviceMsgHisService.getOne(hisQuery);

            // 还没有目标数据，说明发起的请求
            if(targetMsg == null){
                stationDeviceMsgRela.setDeviceId(msgHisRela.getDeviceId());
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
                // 查询对应的数据是否已经在关系表中，在的话说明这是上一条数据，要对当前数据做更新，不存在就做插入
                LambdaQueryWrapper<StationDeviceMsgRela> existQuery = Wrappers.lambdaQuery();
                // 目标数据是由碳表发起的上报，则这条消息应该是主站对碳表上报的回复
                if(targetMsg.getDir() == 1 && targetMsg.getPrm() == 0 && msgHisRela.getDir() == 0 && msgHisRela.getPrm() == 0){
                    existQuery.eq(StationDeviceMsgRela::getDeviceId,targetMsg.getDeviceId());
                    existQuery.eq(StationDeviceMsgRela::getDeviceMsgId,targetMsg.getId());
                    StationDeviceMsgRela one = this.getOne(existQuery);
                    if(one == null){
                        stationDeviceMsgRela.setDeviceId(msgHisRela.getDeviceId());
                        stationDeviceMsgRela.setDir(1);
                        stationDeviceMsgRela.setDeviceMsgId(msgHisRela.getId());
                        this.save(stationDeviceMsgRela);
                    }else{
                        one.setMainStationMsgId(msgHisRela.getId());
                        this.updateById(one);
                    }
                }

                // 目标数据是主站发起的请求，则这条消息是碳表对主站的回复
                if(targetMsg.getDir() == 0 && targetMsg.getPrm() == 1 && msgHisRela.getDir() == 1 && msgHisRela.getPrm() == 1){
                    existQuery.eq(StationDeviceMsgRela::getDeviceId,targetMsg.getDeviceId());
                    existQuery.eq(StationDeviceMsgRela::getMainStationMsgId,targetMsg.getId());
                    StationDeviceMsgRela one = this.getOne(existQuery);
                    if(one == null){
                        stationDeviceMsgRela.setDeviceId(msgHisRela.getDeviceId());
                        stationDeviceMsgRela.setDir(0);
                        stationDeviceMsgRela.setMainStationMsgId(msgHisRela.getId());
                        this.save(stationDeviceMsgRela);
                    }else{
                        one.setDeviceId(msgHisRela.getId());
                        this.updateById(one);
                    }
                }

            }
        }catch (Exception e){
            log.error("关系查找异常",e);
        }


    }
}
