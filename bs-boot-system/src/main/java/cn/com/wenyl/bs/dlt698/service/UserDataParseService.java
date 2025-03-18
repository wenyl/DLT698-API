package cn.com.wenyl.bs.dlt698.service;

import cn.com.wenyl.bs.dlt698.entity.APDU;
import cn.com.wenyl.bs.dlt698.entity.FrameData;
import cn.com.wenyl.bs.dlt698.entity.OAD;
import org.json.JSONObject;

public interface UserDataParseService {
    JSONObject parseUserData(FrameData frameData);
    APDU getApdu(byte[] userData);
    OAD parseOAD(byte[] oad);
    void getObjectOneAttr(byte[] responseData);
}
