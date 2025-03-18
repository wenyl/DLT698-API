package cn.com.wenyl.bs.system.sysResource.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2024年03月13日 13:19
 */
@Data
public class SysResourceSelectTreeVo {
    private String id;
    private String label;
    private String value;
    private Double sortNo;
    private Boolean isLeaf;
    private List<SysResourceSelectTreeVo> children;
}
