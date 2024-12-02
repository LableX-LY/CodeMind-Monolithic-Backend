package com.xly.codemind.exception;

import com.xly.codemind.common.ErrorCode;
import lombok.Data;

/**
 * @author X-LY。
 * @version 1.0
 * @className BusinessException
 * @description
 **/
@Data
public class BusinessException extends RuntimeException {

    /**
     * 我们不需要去自己设置这个异常累的相关信息，所以定义为final
     */
    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

}
