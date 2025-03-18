package cn.com.wenyl.bs.dlt698.utils;

import cn.com.wenyl.bs.dlt698.entity.CSInfo;

public class CSInfoUtils {
    public static void copyControlDomainInfo(CSInfo target, CSInfo source) {
        target.setFunCode(source.getFunCode());
        target.setSc(source.getSc());
        target.setFrameFlg(source.getFrameFlg());
        target.setPrm(source.getPrm());
        target.setDir(source.getDir());
    }

    public static void copyAddressDomainInfo(CSInfo target, CSInfo source) {
        target.setAType(source.getAType());
        target.setLa(source.getLa());
        target.setSa(source.getSa());
        target.setCa(source.getCa());
        target.setHasLogicAddress(source.isHasLogicAddress());
        target.setAddressLength(source.getAddressLength());
    }
}
