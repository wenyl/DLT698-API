package cn.com.wenyl.bs.dlt698.client.service.impl;

import cn.com.wenyl.bs.dlt698.client.service.BaseFrameParser;
import cn.com.wenyl.bs.dlt698.client.service.LengthDomainBuildService;
import cn.com.wenyl.bs.dlt698.common.Frame;
import cn.com.wenyl.bs.dlt698.common.LinkUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("unchecked")
public class FrameParserFactory<T extends Frame,G extends LinkUserData> {
    private static final Map<Class<?>, BaseFrameParser<? extends Frame, ? extends LinkUserData>> frameParserMap = new HashMap<>();


    // 构造函数：自动注入所有的解析器并根据泛型类型推断帧类型
    @Autowired
    public FrameParserFactory(List<BaseFrameParser<T,G>> frameParsers) {
        for (BaseFrameParser<T,G> parser : frameParsers) {
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
    public BaseFrameParser<T, G> getFrameParser(Class<T> frameType) {
        // 使用通配符获取解析器
        BaseFrameParser<?, ?> parser = frameParserMap.get(frameType);

        if (parser == null) {
            throw new IllegalArgumentException("No parser found for " + frameType.getSimpleName());
        }

        // 强制转换为合适的类型
        @SuppressWarnings("unchecked")
        BaseFrameParser<T, G> typedParser = (BaseFrameParser<T, G>) parser;

        return typedParser;
    }
}
