package cn.com.wenyl.bs.system.sysResource.service;

import cn.com.wenyl.bs.system.auth.vo.MenuVo;
import cn.com.wenyl.bs.system.auth.vo.RouterVo;
import cn.com.wenyl.bs.system.sysResource.entity.SysResource;
import cn.com.wenyl.bs.system.sysResource.vo.SysMenuTreeVo;
import cn.com.wenyl.bs.system.sysResource.vo.SysResourceSelectTreeVo;
import cn.com.wenyl.bs.system.sysResource.vo.SysResourceTableTreeVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISysResourceService extends IService<SysResource> {

    /**
     * 获取用户的按钮权限
     * @param userId 用户ID
     * @return 用户的按钮权限
     */
    List<SysResource> getUserBtn(String userId);
    /**
     * 获取用户的菜单路由权限,用于前端初始化路由
     * @param userId 用户ID
     * @return 用户的菜单权限
     */
    List<RouterVo> getUserRouter(String userId);

    /**
     * 根据ID删除资源树中的资源
     * @param id 数据id
     */
    void removeTreeDataById(String id);

    /**
     * 获取系统菜单树
     * @return 获取系统菜单树
     */
    List<SysMenuTreeVo> sysMenuTree();

    /**
     * 获取系统资源树
     * @return 获取系统资源树
     */
    List<SysResourceTableTreeVo> getSysResourceTableTree();

    /**
     * 获取资源信息，并以element tree所需得格式返回
     * @return 返回资源信息
     */
    List<SysResourceSelectTreeVo> getSysResourceSelectTree();
    /**
     * 获取用户的菜单信息,用于前端初始化菜单
     * @param userId 用户ID
     * @return 用户的菜单权限
     */
    List<MenuVo> getUserMenu(String userId);
}