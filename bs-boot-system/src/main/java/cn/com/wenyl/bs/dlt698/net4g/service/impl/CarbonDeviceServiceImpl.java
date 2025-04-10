package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.net4g.entity.CarbonDevice;
import cn.com.wenyl.bs.dlt698.net4g.mapper.CarbonDeviceMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 碳表信息 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Service
public class CarbonDeviceServiceImpl extends ServiceImpl<CarbonDeviceMapper, CarbonDevice> implements CarbonDeviceService {

    @Override
    public void isAlive(Integer deviceId) {
        LambdaUpdateWrapper<CarbonDevice> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(CarbonDevice::getDeviceStatus,1);
        updateWrapper.eq(CarbonDevice::getId,deviceId);
        this.update(updateWrapper);
    }

    @Override
    public void isDead(Integer deviceId) {
        LambdaUpdateWrapper<CarbonDevice> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(CarbonDevice::getDeviceStatus,0);
        updateWrapper.eq(CarbonDevice::getId,deviceId);
        this.update(updateWrapper);
    }
}
