package cn.com.wenyl.bs.system.sysResource.mapper;

import cn.com.wenyl.bs.system.sysResource.entity.SysResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<SysResource> getUserBtn(@Param("userId") String userId);

    List<SysResource> getUserMenu(@Param("userId") String userId);
}