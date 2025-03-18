package cn.com.wenyl.bs.dlt698.service;

import cn.com.wenyl.bs.dlt698.entity.FrameData;

public interface FrameParseService {
    FrameData parseFrame(byte[] frame);
}
