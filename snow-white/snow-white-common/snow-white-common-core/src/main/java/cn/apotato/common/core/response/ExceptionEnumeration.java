package cn.apotato.common.core.response;

import cn.apotato.common.core.exception.NotLoginException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.sql.SQLException;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_SERVICE_UNAVAILABLE;

/**
 * 异常枚举
 *
 * @author 胡晓鹏
 * @date 2023/04/28
 */
public enum ExceptionEnumeration {

    /**
     * 警告例外
     */
    SQL_EXCEPTION(SQLException.class, "数据库操作数据错误"),
    NOT_LOGIN_EXCEPTION(NotLoginException.class, "token无效，请登陆后重试！", SC_SERVICE_UNAVAILABLE),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(MethodArgumentNotValidException.class, "数据库操作数据错误"),
    MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION(MissingServletRequestParameterException.class, "缺少必填参数"),
    ILLEGAL_ARGUMENT_EXCEPTION(IllegalArgumentException.class, "缺少必填参数"),
    DEFAULT_EXCEPTION(RuntimeException.class),
    ;


    final Class<? extends Exception> value;
    public final String message;
    public final Integer code;


    ExceptionEnumeration(Class<? extends Exception> value) {
        this.value = value;
        this.message = null;
        this.code = SC_INTERNAL_SERVER_ERROR;
    }

    ExceptionEnumeration(Class<? extends Exception> value, String message) {
        this.value = value;
        this.message = message;
        this.code = SC_INTERNAL_SERVER_ERROR;
    }

    ExceptionEnumeration(Class<? extends Exception> value, String message, Integer code) {
        this.value = value;
        this.message = message;
        this.code = code;
    }

    /**
     * 得到异常类
     *
     * @param e e
     * @return {@link ExceptionEnumeration}
     */
    public static ExceptionEnumeration getExceptionClass(Exception e) {
        Class<? extends Exception> eClass = e.getClass();
        for (ExceptionEnumeration exceptionEnumeration : ExceptionEnumeration.values()) {
            if (exceptionEnumeration.value.equals(eClass)) {
                return exceptionEnumeration;
            }
            e.printStackTrace();
        }
        return DEFAULT_EXCEPTION;
    }


}
