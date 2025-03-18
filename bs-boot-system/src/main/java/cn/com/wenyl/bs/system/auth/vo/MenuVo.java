package cn.com.wenyl.bs.system.auth.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2024年04月07日 16:32
 */
@Data
public class MenuVo {
    private String name;
    private String path;
    private Meta meta;
    private Double sortNo;
    private List<MenuVo> children;
    public void createMeta(String icon,String title){
        Meta newMeta = new Meta();
        newMeta.setIcon(icon);
        newMeta.setTitle(title);
        this.meta = newMeta;
    }
}
@Data
class Meta{
    private String icon;
    private String title;
}
