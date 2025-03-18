package cn.com.wenyl.bs.system.sysRole.mapper;

import cn.com.wenyl.bs.system.sysRole.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    List<SysRole> getUserRoleList(@Param("userId") String userId);
}