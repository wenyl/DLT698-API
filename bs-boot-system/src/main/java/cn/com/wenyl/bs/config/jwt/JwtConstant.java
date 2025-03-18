package cn.com.wenyl.bs.config.jwt;

/**
 * @author Swimming Dragon
 * @description: 公钥私钥等信息
 * @date 2023年12月06日 13:59
 */
public class JwtConstant {
    /**
     * token过期时间（毫秒）
     */
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 43200000;

    /**
     * 用户登录账号属性名
     */
    public static final String ACCOUNT_PROPERTY_NAME = "username";
    /**
     * token在request header中的属性名
     */
    public static final String REQUEST_HEADER_TOKEN = "BS-WEB-TOKEN";


    /**
     * shiro的token在redis中的缓存key的前缀
     */
    public static final String PREFIX_SHIRO_WEB_TOKEN = "shiro:web_token:";
    /**
     * token不存在或者无效的返回信息
     */
    public static final String TOKEN_IS_INVALID_MSG = "请先登录";
}
