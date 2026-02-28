package com.chao.failure_in_action.model.enums;


import com.chao.failfast.internal.core.ResponseCode;

/**
 * 请求错误码
 * @author Chao
 * @Github <a href="https://github.com/KyrieChao/Failure">Failure</a>
 */
public enum RequestCode implements ResponseCode {

    // 请求对象不能为空
    REQUEST_OBJECT_NOT_NULL(10001, "Request object cannot be null", "请求对象不能为空");

    RequestCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    private final int code;
    private final String message;
    private final String description;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
