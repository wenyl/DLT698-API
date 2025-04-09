package cn.com.wenyl.bs.dlt698.common.entity.dto;

import cn.com.wenyl.bs.dlt698.common.constants.ClientAPDU;
import cn.com.wenyl.bs.dlt698.common.constants.RequestType;
import cn.com.wenyl.bs.dlt698.common.constants.ServerAPDU;
import cn.com.wenyl.bs.dlt698.common.entity.OAD;
import lombok.Data;

@Data
public class MsgDto {
    private int id;
    private String deviceId;
    private RequestType requestType;
    private ClientAPDU clientAPDU;
    private ServerAPDU serverAPDU;
    private OAD oad;
}
