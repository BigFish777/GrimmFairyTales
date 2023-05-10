package cn.apotato.common.core.response;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static cn.apotato.common.core.response.ResultCode.*;
import static javax.servlet.http.HttpServletResponse.*;


/**
 * 全局响应机构处理器
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@Slf4j
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
        if (status >= SC_OK && status < SC_MULTIPLE_CHOICES) {
            //当返回对象为ResponseResult时,直接返回
            if (body instanceof ResponseResult) {
                ResponseResult result = (ResponseResult) body;
                if (result.getCode() == SC_INTERNAL_SERVER_ERROR) {
                    servletResponse.setStatus(SC_INTERNAL_SERVER_ERROR);
                }
                if (result.getCode() == SC_SERVICE_UNAVAILABLE) {
                    servletResponse.setStatus(SC_SERVICE_UNAVAILABLE);
                }
                return body;
            }
            if (body instanceof String) {
                return JSONUtil.toJsonStr(new ResponseResultData<>(SUCCESS, body).setCode(status));
            }
            return new ResponseResultData<>(SUCCESS, body).setCode(status);
        }

        //响应状态码为[400-500)的范围时的处理
        if (status >= SC_BAD_REQUEST && status < SC_INTERNAL_SERVER_ERROR) {
            if (status == SC_NOT_FOUND) {
                return new ResponseResult(CLIENT_URL_FAIL);
            }
            return new ResponseResult(CLIENT_FAIL).setCode(status);
        }
        return new ResponseResult(SUCCESS).setCode(status);
    }


}
