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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 设备消息历史关系 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Slf4j
@Service

public class StationDeviceMsgRelaServiceImpl extends ServiceImpl<StationDeviceMsgRelaMapper, StationDeviceMsgRela> implements StationDeviceMsgRelaService {

    @Resource
    @Lazy
    private DeviceMsgHisService deviceMsgHisService;
    private DeviceMsgHis findTargetMsg(DeviceMsgHisRela msgHisRela){
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
        return deviceMsgHisService.getOne(hisQuery);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRelaMsg(DeviceMsgHisRela msgHisRela) {
        try{
            StationDeviceMsgRela stationDeviceMsgRela = new StationDeviceMsgRela();
            // 碳表给主站发送的请求，不存在回复,直接保存即可
            if(msgHisRela.getDir() == 1 && msgHisRela.getPrm() == 0){
                stationDeviceMsgRela.setDeviceId(msgHisRela.getDeviceId());
                stationDeviceMsgRela.setDeviceMsgId(msgHisRela.getId());
                stationDeviceMsgRela.setDir(1);
                this.save(stationDeviceMsgRela);
                return;
            }
            // 此时msgHisRela是主站给碳表的回复，需要找到碳表发给主站的请求，这里由于有很多相同的请求，可能会查询到多条数据，取时间最新的一条
            if(msgHisRela.getDir() == 0 && msgHisRela.getPrm() == 0){
                DeviceMsgHis targetMsg = findTargetMsg(msgHisRela);
                if(targetMsg == null){
                    log.error("关系存储异常,主站响应的消息id={}找不到对应请求",msgHisRela.getId());
                    return;
                }
                // 查询目标消息是否已经被绑定
                LambdaQueryWrapper<StationDeviceMsgRela> findRela = Wrappers.lambdaQuery();
                findRela.eq(StationDeviceMsgRela::getDeviceId,msgHisRela.getDeviceId());
                findRela.eq(StationDeviceMsgRela::getDeviceMsgId,targetMsg.getId());
                StationDeviceMsgRela one = this.getOne(findRela);
                if(one.getMainStationMsgId() != null){
                    log.error("关系存储异常,主站响应的消息id={},碳表请求的消息id={}在关系表中已经被其他主站回复的消息绑定,关系表id={},已绑定的消息id={}",msgHisRela.getId(),targetMsg.getId(),one.getId(),one.getMainStationMsgId());
                    return;
                }
                one.setMainStationMsgId(msgHisRela.getId());
                this.updateById(one);
            }
            // 这是主站发起的请求，直接保存即可
            if(msgHisRela.getDir() == 0 && msgHisRela.getPrm() == 1){
                stationDeviceMsgRela.setDeviceMsgId(msgHisRela.getDeviceId());
                stationDeviceMsgRela.setMainStationMsgId(msgHisRela.getId());
                stationDeviceMsgRela.setDir(0);
                this.save(stationDeviceMsgRela);
                return;
            }
            // 这是碳表对主站请求的响应
            if(msgHisRela.getDir() == 1 && msgHisRela.getPrm() == 1){
                DeviceMsgHis targetMsg = findTargetMsg(msgHisRela);
                if(targetMsg == null){
                    log.error("关系存储异常,碳表响应的消息id={}找不到对应请求",msgHisRela.getId());
                    return;
                }
                LambdaQueryWrapper<StationDeviceMsgRela> findRela = Wrappers.lambdaQuery();
                findRela.eq(StationDeviceMsgRela::getDeviceId,msgHisRela.getDeviceId());
                findRela.eq(StationDeviceMsgRela::getMainStationMsgId,targetMsg.getId());
                StationDeviceMsgRela one = this.getOne(findRela);
                if(one.getDeviceMsgId() != null){
                    log.error("关系存储异常,碳表响应的消息id={},主站请求的消息id={}在关系表中已经被其他碳表回复的消息绑定,关系表id={},已绑定的消息id={}",msgHisRela.getId(),targetMsg.getId(),one.getId(),one.getDeviceId());
                    return;
                }
                one.setDeviceId(msgHisRela.getId());
                this.updateById(one);
            }
        }catch (Exception e){
            log.error("关系查找异常",e);
        }


    }
}
