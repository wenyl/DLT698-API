package cn.com.wenyl.bs.system.sysResource.vo;

import cn.com.wenyl.bs.system.sysRoleResource.entity.SysRoleResource;
import lombok.Data;

import java.util.List;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2024年03月21日 16:15
 */
@Data
public class SysRoleResourcesSelectTreeVo {
    private List<SysResourceSelectTreeVo> selectTreeVo;
    private List<SysRoleResource> selectedResources;
}
