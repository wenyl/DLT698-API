package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.entity.Frame;
import cn.com.wenyl.bs.dlt698.service.BaseFrameParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("unchecked")
public class FrameParserFactory {
    private static final Map<Class<? extends Frame>, BaseFrameParser<? extends Frame>> frameParserMap = new HashMap<>();

    // 构造函数：自动注入所有的解析器并根据泛型类型推断帧类型
    @Autowired
    public FrameParserFactory(List<BaseFrameParser<?>> frameParsers) {
        for (BaseFrameParser<?> parser : frameParsers) {
            Type[] genericInterfaces = parser.getClass().getGenericInterfaces();

            // 确保解析器实现类实现了泛型接口并能够正确提取泛型类型
            if (genericInterfaces.length > 0 && genericInterfaces[0] instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
                // 获取泛型的第一个类型参数（即Frame的子类类型）
                Class<? extends Frame> frameType = (Class<? extends Frame>) parameterizedType.getActualTypeArguments()[0];
                // 将帧类型和对应的解析器映射到map中
                frameParserMap.put(frameType, parser);
            } else {
                throw new IllegalArgumentException("BaseFrameParser must have a parameterized type argument");
            }
        }
    }

    // 通过帧类型返回相应的解析器
    public <T extends Frame> BaseFrameParser<T> getFrameParser(Class<T> frameType) {
        BaseFrameParser<T> parser = (BaseFrameParser<T>) frameParserMap.get(frameType);
        if (parser == null) {
            throw new IllegalArgumentException("No parser found for " + frameType.getSimpleName());
        }
        return parser;
    }
}
