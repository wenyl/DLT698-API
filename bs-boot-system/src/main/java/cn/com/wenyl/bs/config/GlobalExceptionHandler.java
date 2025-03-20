package cn.com.wenyl.bs.config;

import cn.com.wenyl.bs.exceptions.GeneratorCodeException;
import cn.com.wenyl.bs.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * @author Swimming Dragon
 * @description: 全局异常处理器
 * @date 2023年12月04日 21:24
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GeneratorCodeException.class)
    public R<String> handleGeneratorCodeException(GeneratorCodeException e){
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }

    /**
     * 身份验证失败、授权失败、token无效
     * @param e  异常
     * @return 返回异常信息，状态码403
     */
    @ExceptionHandler(AuthorizationException.class)
    public R<String> handleException2(AuthorizationException e) {
        log.error(e.getMessage());
        return R.error(HttpStatus.SC_UNAUTHORIZED,e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public R<?> handleException(Exception e){
        log.error(e.getMessage(), e);
        return R.error(StringUtils.isBlank(e.getMessage())?"操作失败,请联系管理员":e.getMessage());
    }
}
