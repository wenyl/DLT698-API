package cn.com.wenyl.bs.system.auth.vo;

import cn.com.wenyl.bs.system.sysResource.entity.SysResource;
import lombok.Data;

import java.util.List;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2024年01月15日 10:02
 */
@Data
public class SysUserResourceVo {
    private static final long serialVersionUID = 1L;
    private List<RouterVo> routerList;
    private List<SysResource> btnList;
    private List<MenuVo> menuList;
}
