package cn.com.wenyl.bs.dlt698.common.entity;

import cn.com.wenyl.bs.dlt698.common.constants.AttrNum;
import cn.com.wenyl.bs.dlt698.common.constants.AttributeIndex;
import cn.com.wenyl.bs.dlt698.common.constants.OI;
import lombok.Data;

/**
 * 帧OAD信息
 */
@Data
public class OAD {
    private OI oi;
    private AttrNum attrNum;
    private AttributeIndex attributeIndex;
}
