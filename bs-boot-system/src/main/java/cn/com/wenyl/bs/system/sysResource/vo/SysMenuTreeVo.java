package cn.com.wenyl.bs.system.sysResource.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2024年01月25日 13:26
 */
@Data
public class SysMenuTreeVo {
    private String id;
    private String label;
    private String value;
    private Double sortNo;
    private Boolean isLeaf;
    private List<SysMenuTreeVo> children;
}
