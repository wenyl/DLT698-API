package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.service.BaseFrameBuilder;
import cn.com.wenyl.bs.dlt698.common.entity.Frame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FrameBuilderFactory {
    private static final Map<String, BaseFrameBuilder<?>> frameBuilderMap = new HashMap<>();

    @Autowired
    public FrameBuilderFactory(List<BaseFrameBuilder<?>> frameBuilders) {
        for (BaseFrameBuilder<?> builder : frameBuilders) {
            // 获取 builder 实现类的泛型类型
            Class<?> frameType = (Class<?>) ((ParameterizedType) builder.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            frameBuilderMap.put(frameType.getName(), builder);
        }
    }

    // 通过泛型类型 T 自动选择对应的 Builder
    @SuppressWarnings("unchecked")
    public <T extends Frame> BaseFrameBuilder<T> getFrameBuilder(Class<T> frameType) {

        BaseFrameBuilder<T> builder = (BaseFrameBuilder<T>) frameBuilderMap.get(frameType.getName());
        if (builder == null) {
            throw new IllegalArgumentException("No builder found for " + frameType.getSimpleName());
        }
        return builder;

    }
}
