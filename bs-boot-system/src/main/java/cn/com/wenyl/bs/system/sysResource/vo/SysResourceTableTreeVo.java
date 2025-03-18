package cn.com.wenyl.bs.system.sysResource.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2024年03月04日 14:15
 */
@Data
public class SysResourceTableTreeVo implements Serializable {
    private String id;
    private String parentId;
    private String resourceName;
    private String menuUrl;
    private String menuComponent;
    private String redirect;
    private Integer resourceType;
    private String perms;
    private Double sortNo;
    private String menuIcon;
    private Short isRoute;
    private Short isLeaf;
    private String description;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private Integer delFlag;
    private List<SysResourceTableTreeVo> children;
}
