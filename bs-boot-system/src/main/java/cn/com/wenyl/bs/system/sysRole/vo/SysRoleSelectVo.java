package cn.com.wenyl.bs.system.sysRole.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2024年03月12日 13:49
 */
@Data
public class SysRoleSelectVo implements Serializable {
    private String id;
    private String label;
    private String value;
}
