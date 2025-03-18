package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.entity.CSInfo;
import cn.com.wenyl.bs.dlt698.service.AddressDomainParseService;
import cn.com.wenyl.bs.dlt698.utils.BCDUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service("addressDomainParseService")
public class AddressDomainParseServiceImpl implements AddressDomainParseService {
    @Override
    public CSInfo parseAddressDomain(byte[] addressDomain) {
        CSInfo csInfo = new CSInfo();
        int addressField = addressDomain[0] & 0xFF;
        int addressLength = (addressField & 0x0F) + 1;  // bit0-bit3：地址长度
        csInfo.setAddressLength(addressLength);
        // 判断有无逻辑地址
        int hasLogicalAddress = (addressField >>> 5) & 0x01;
        byte[] extendLogicAddress;
        byte[] serverAddress;
        if (hasLogicalAddress==1) {
            extendLogicAddress = Arrays.copyOfRange(addressDomain,1,2);
            serverAddress = Arrays.copyOfRange(addressDomain,2,addressDomain.length-1);
            csInfo.setHasLogicAddress(true);
            csInfo.setLa(extendLogicAddress[0]);
        }else{
            serverAddress = Arrays.copyOfRange(addressDomain,1,addressDomain.length-1);
            csInfo.setHasLogicAddress(false);
        }
        csInfo.setSa(serverAddress);
        byte clientAddress = addressDomain[addressDomain.length-1];
        csInfo.setCa(clientAddress);
        int addressType = (addressField >>> 6) & 0x03;
        csInfo.setAType(addressType);
        return csInfo;
    }
}
