package cn.com.wenyl.bs.dlt698.service;

import cn.com.wenyl.bs.dlt698.entity.APDU;

public interface UserDataBuildService {
    byte[] buildUserData(APDU apdu);
}
