package cn.com.wenyl.bs.system.auth.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2024年01月09日 11:01
 */
@Data
public class RouterVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String path;
    private String component;
    private String redirect;

    private List<RouterVo> children;
    private Double sortNo;
}

