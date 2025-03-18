package cn.com.wenyl.bs.config.jwt;

import cn.com.wenyl.bs.utils.R;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author Swimming Dragon
 * @description: jwt工具类
 * @date 2023年12月06日 13:24
 */
public class JwtUtil {

    /**
     * 构建一个token
     * @param username 用户名
     * @param password 用户密码
     * @return 返回token
     */
    public static String createToken(String username,String password) throws UnsupportedEncodingException {
        Date date = new Date(System.currentTimeMillis() + JwtConstant.ACCESS_TOKEN_EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(password);
        return JWT.create().withClaim(JwtConstant.ACCOUNT_PROPERTY_NAME, username).withExpiresAt(date).sign(algorithm);
    }

    /**
     * 校验token
     * @param token token
     * @param username 用户名
     * @param password 用户密码
     * @return 返回token是否合法
     */
    public static boolean verifyToken(String token, String username, String password) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm).withClaim(JwtConstant.ACCOUNT_PROPERTY_NAME, username).build();
            // 效验TOKEN
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 根据token获取用户名
     * @param token token
     * @return 用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(JwtConstant.ACCOUNT_PROPERTY_NAME).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * response 响应错误
     * @param response 响应
     * @param code 错误编码
     * @param errorMsg 错误信息
     */
    public static void responseError(ServletResponse response, Integer code, String errorMsg) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // issues/I4YH95浏览器显示乱码问题
        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        R ret = R.error(code,errorMsg);
        OutputStream os;
        try {
            os = httpServletResponse.getOutputStream();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setStatus(code);
            os.write(new ObjectMapper().writeValueAsString(ret).getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
