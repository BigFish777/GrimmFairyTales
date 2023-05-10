package cn.apotato.common.core.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@SuppressWarnings("unused")
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 请求状态
     */
    private Boolean success;

    /**
     * 响应状态
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    public ResponseResult(ResultCode resultCode) {
        this.success = resultCode.getSuccess();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public static ResponseResult SUCCESS() {
        return new ResponseResult(ResultCode.SUCCESS);
    }

    public static ResponseResult FAIL() {
        return new ResponseResult(ResultCode.SERVER_FAIL);
    }

    public ResponseResult setMessage(String message) {
        this.message = message;
        return this;
    }
}
