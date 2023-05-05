package cn.apotato.common.core.exception;

import cn.apotato.common.core.response.ExceptionEnumeration;
import cn.apotato.common.core.response.ResponseResult;
import cn.apotato.common.core.response.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局异常处理
 *
 * @author 胡晓鹏
 * @date 2023/04/28
 */
@RestControllerAdvice
@SuppressWarnings("NullableProblems")
public class GlobalExceptionHandling {

    /**
     * 异常处理
     *
     * @param e e
     * @return {@link ResponseResult}
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandling(Exception e) {
        ResponseResult result = new ResponseResult(ResultCode.SERVER_FAIL);
        ExceptionEnumeration exceptionEnumeration = ExceptionEnumeration.getExceptionClass(e);
        if (exceptionEnumeration.code != null) {
            result.setCode(exceptionEnumeration.code);
        }
        if (StringUtils.isNotBlank(exceptionEnumeration.message)) {
            result.setMessage(exceptionEnumeration.message);
        }
        e.printStackTrace();
        return result;
    }


    /**
     * Gets msg by method argument not valid exception.
     * 从Spring的Valid异常中提取信息
     *
     * @param e the e
     * @return the msg by method argument not valid exception
     */
    private String getMsgByMethodArgumentNotValidException(Exception e) {
        String pattern = "\\[[-\\w\\u4e00-\\u9fa5]*]]\\s*$";
        Pattern compile = Pattern.compile(pattern);
        Matcher m = compile.matcher(e.getMessage());
        if (m.find()) {
            String msg = m.group();
            return msg.substring(1, msg.length() - 3);
        }
        return null;
    }

}
