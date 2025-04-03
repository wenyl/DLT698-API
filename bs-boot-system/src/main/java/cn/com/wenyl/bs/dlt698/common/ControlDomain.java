package cn.com.wenyl.bs.dlt698.common;

import lombok.Data;

/**
 * 控制域
 */
@Data
public class ControlDomain {
    private int funCode;    // 功能码
    private int sc;         // 安全控制
    private int frameFlg;  // 分帧标志
    private int prm;        // 启动标志
    private int dir;        // 方向
}
