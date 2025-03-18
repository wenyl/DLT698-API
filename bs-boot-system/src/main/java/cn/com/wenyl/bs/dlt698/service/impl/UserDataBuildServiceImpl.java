package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.entity.APDU;
import cn.com.wenyl.bs.dlt698.service.UserDataBuildService;
import org.springframework.stereotype.Service;

@Service("/userDataBuildService")
public class UserDataBuildServiceImpl implements UserDataBuildService {
    @Override
    public byte[] buildUserData(APDU apdu) {
        byte[] userData = new byte[apdu.getByteLength()];
        userData[0] = apdu.getApdu();
        userData[1] = apdu.getRequest();
        userData[2] = apdu.getPiid();
        byte[] oad = apdu.getOad();
        int oadLength = oad.length;
        System.arraycopy(oad, 0, userData, 3, oadLength);
        userData[apdu.getByteLength() - 1] = apdu.getTimeTag();
        return userData;
    }
}
