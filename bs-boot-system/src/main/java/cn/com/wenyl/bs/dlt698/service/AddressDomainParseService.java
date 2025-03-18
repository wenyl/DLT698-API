package cn.com.wenyl.bs.dlt698.service;

import cn.com.wenyl.bs.dlt698.entity.CSInfo;

public interface AddressDomainParseService {
    CSInfo parseAddressDomain(byte[] controlDomain);
}
