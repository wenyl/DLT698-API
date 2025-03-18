package cn.com.wenyl.bs.system.auth.vo;

import lombok.Data;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2024年01月08日 16:28
 */
@Data
public class SysResourceVo {
    private static final long serialVersionUID = 1L;
    private String id;
    private String resourceName;
    private String perms;
}
