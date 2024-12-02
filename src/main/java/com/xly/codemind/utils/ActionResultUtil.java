package com.xly.codemind.utils;

import com.xly.codemind.common.BaseResponse;
import com.xly.codemind.common.ErrorCode;

/**
 * @author X-LY。
 * @version 1.0
 * @className ActionResultUtil
 * @description 全局通用的生成返沪结果的工具类
 **/

public class ActionResultUtil {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(200,data,"success");
    }

    public static <T> BaseResponse<T> fail(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    public static <T> BaseResponse<T> error(int code, String message,String description) {
        return new BaseResponse<>(code, null, message, description);
    }

}
