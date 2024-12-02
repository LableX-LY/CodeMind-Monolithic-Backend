package com.xly.codemind.exception;

import com.xly.codemind.common.BaseResponse;
import com.xly.codemind.common.ErrorCode;
import com.xly.codemind.utils.ActionResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author X-LY。
 * @version 1.0
 * @className GlobalExceptionHandler
 * @description 全局异常处理器，抛出的异常由该类处理（转换为BaseResponse）
 **/

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException",e.getMessage(),e);
        return ActionResultUtil.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runTimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException",e);
        return ActionResultUtil.error(ErrorCode.SYSTEM_ERROR, e.getMessage(),"");
    }

}
