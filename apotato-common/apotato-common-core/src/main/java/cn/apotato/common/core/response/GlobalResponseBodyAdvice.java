package cn.apotato.common.core.response;

import cn.hutool.json.JSONUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 全局响应机构处理器
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@RestControllerAdvice
@SuppressWarnings("NullableProblems")
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Method method = returnType.getMethod();
        return null != method && Void.class != method.getReturnType();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //获得HttpServletResponse对象并通过该对象获得响应状态码，也可以通过该对象自己定义返回的状态码
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        //响应状态码为[200-300)的范围时的处理
        if (status >= HttpServletResponse.SC_OK && status < HttpServletResponse.SC_MULTIPLE_CHOICES) {
            //当返回对象为ResponseResult时,直接返回
            if (body instanceof ResponseResult) {
                return body;
            }

            //获得returnType的返回类型
            String returnTypeName = returnType.getParameterType().getName();
            if ("java.lang.String".equals(returnTypeName)) {
                return JSONUtil.toJsonStr(new ResponseResultData<>(ResultCode.SUCCESS, body).setCode(status));
            }
            return new ResponseResultData<>(ResultCode.SUCCESS, body).setCode(status);
        }

        //响应状态码为[400-500)的范围时的处理
        if (status >= HttpServletResponse.SC_BAD_REQUEST && status < HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            if (status == HttpServletResponse.SC_NOT_FOUND) {
                return new ResponseResult(ResultCode.CLIENT_URL_FAIL);
            }
            return new ResponseResult(ResultCode.CLIENT_FAIL).setCode(status);
        }
        return new ResponseResult(ResultCode.SUCCESS).setCode(status);
    }


    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionDeal(Exception e) {
        ResponseResult result = new ResponseResult(ResultCode.SERVER_FAIL);
        if (e instanceof SQLException) {
            result.setMessage("数据库操作数据错误");
        } else if (e instanceof MethodArgumentNotValidException) {
            result.setMessage(getMsgByMethodArgumentNotValidException(e));
        } else if (e instanceof MissingServletRequestParameterException) {
            result.setMessage("缺少必填参数");
        } else if (e instanceof IllegalArgumentException) {
            result.setMessage(e.getMessage());
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
