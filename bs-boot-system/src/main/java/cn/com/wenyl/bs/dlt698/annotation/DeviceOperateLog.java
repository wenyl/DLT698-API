package cn.com.wenyl.bs.dlt698.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识DLT698接口的名称返回值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DeviceOperateLog {
    /**
     * 任务名称
     * @return 任务名称
     */
    String jobName();
    /**
     * 返回值标识符
     * @return 返回值标识符
     */
    String valueSign();
    /**
     * 返回值标签
     * @return 返回值标签
     */
    String valueLabel();
    /**
     * 是否有返回值，这里用于区别调用DLT698接口的方法和DLT698接口方法，调用DLT698接口的方法是不会产生报文和返回值的，只有DLT698接口本身的方法才会有发送\接收报文\返回值
     * true标识这是
     * @return 是否有返回值
     */
    boolean hasValue();

    /**
     * 标识是否大屏数据，是的话就需要标识为true
     * @return 是否大屏数据
     */
    boolean screenData() default false;
}
