package cn.com.wenyl.bs.dlt698.client.service.impl;

import cn.com.wenyl.bs.dlt698.common.service.BaseFrameParser;
import cn.com.wenyl.bs.dlt698.common.entity.Frame;
import cn.com.wenyl.bs.dlt698.common.entity.LinkUserData;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
@SuppressWarnings("unchecked")
public class FrameParserFactory{
    private final Map<String, BaseFrameParser<? extends Frame, ? extends LinkUserData>> parsers = new HashMap<>();

    @Autowired
    public FrameParserFactory(List< BaseFrameParser<?, ?>> parsersList) {
        // 遍历 List，将其转换为 Map
        for (BaseFrameParser<?, ?> parser : parsersList) {
            // 获取泛型类型的 Class 对象
            Class<?>[] genericTypes = getParserGenericTypes(parser);
            // 生成唯一的 key
            String key = generateKey(genericTypes[0], genericTypes[1]);
            parsers.put(key, parser);
        }
    }

    public <T extends Frame, G extends LinkUserData> BaseFrameParser<T, G> getParser(Class<T> frameClass, Class<G> userDataClass) {
        // 根据类型生成唯一的键
        String key = generateKey(frameClass, userDataClass);

        // 查找并返回相应的解析器
        BaseFrameParser<?, ?> parser = parsers.get(key);

        if (parser == null) {
            String msg = "找不到解析器 frame类型为="+frameClass.getName()+",linkUserData类型="+userDataClass.getName();
            log.error(msg);
            throw new RuntimeException(msg);
        }

        // 类型转换，确保返回正确的解析器类型
        return (BaseFrameParser<T, G>) parser;
    }
    // 根据 Frame 和 LinkUserData 类型生成唯一的 key
    private String generateKey(Class<?> frameClass, Class<?> userDataClass) {
        return frameClass.getName() + "-" + userDataClass.getName();
    }
    // 获取 BaseFrameParser 实例的泛型类型
    private Class<?>[] getParserGenericTypes(BaseFrameParser<?, ?> parser) {
        // 通过反射获取 parser 的泛型类型
        java.lang.reflect.ParameterizedType type = (java.lang.reflect.ParameterizedType) parser.getClass().getGenericInterfaces()[0];
        Class<?> frameClass = (Class<?>) type.getActualTypeArguments()[0];
        Class<?> userDataClass = (Class<?>) type.getActualTypeArguments()[1];
        return new Class<?>[]{frameClass, userDataClass};
    }

}
