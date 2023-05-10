package cn.apotato.common.core.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ResponseResultData<T> extends ResponseResult {

    private T data;

    public ResponseResultData(ResultCode resultCode, T t) {
        super(resultCode);
        this.data = t;
    }

}
