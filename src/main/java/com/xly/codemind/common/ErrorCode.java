package com.xly.codemind.common;

/**
 * @author X-LY。
 * @version 1.0
 * @className ErrorCode
 * @description 全局通用的错误码
 **/
public enum ErrorCode {

    SUCCESS(0,"success",""),

    PARAMS_ERROR(40000,"请求参数错误",""),

    PARAMS_NULL_ERROR(40001,"请求参数为空",""),

    NOT_LOGIN_ERROR(40100,"未登录",""),

    NO_AUTH_ERROR(40101,"无权限",""),

    USER_BAN_ERROR(40105,"用户被禁",""),

    QUESTION_BAN_ERROR(40106,"题目被禁用",""),

    OPERATION_ERROR(40107,"题目正在判题中",""),

    SYSTEM_ERROR(50000,"系统内部异常",""),

    API_REQUEST_ERROR(50010, "接口调用失败",""),

    NULL_ERROR(40002,"查询结果为空","");

    private final int code;

    private final String message;

    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
