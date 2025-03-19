package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.entity.ConnectRequestFrame;
import cn.com.wenyl.bs.dlt698.service.BaseFrameBuilder;
import org.springframework.stereotype.Service;

@Service("connectRequestBuilder")
public class ConnectRequestBuilder extends BaseFrameBuilderImpl<ConnectRequestFrame> implements BaseFrameBuilder<ConnectRequestFrame> {

    @Override
    public byte[] buildFrame(ConnectRequestFrame frame) {
        return new byte[0];
    }

    @Override
    public byte[] buildLinkUserData(ConnectRequestFrame frame) {
        return new byte[0];
    }
}
