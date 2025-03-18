package cn.com.wenyl.bs.system.sysRoleResource.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2024年03月13日 15:28
 */
@Data
public class RoleResourcesDto {
    private String roleId;
    private List<String> checkedKeys;
    private List<String> halfCheckedKeys;
}
