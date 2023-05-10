package cn.apotato.common.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(true, HttpStatus.OK.value(), "请求成功"),
    SERVER_FAIL(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "请求失败，服务端错误"),
    CLIENT_URL_FAIL(false, HttpStatus.BAD_REQUEST.value(), "请检查URL是否正确"),
    CLIENT_FAIL(false, HttpStatus.BAD_REQUEST.value(), "请求失败，客户端错误");

    private final Boolean success;

    private final Integer code;

    private final String message;

}
