package com.chao.failure_in_action.model.enums;

import com.chao.failfast.internal.ResponseCode;

public enum UserCode implements ResponseCode {
    USERNAME_EXIST(400_01, "用户名已存在", "该用户名已被注册"),
    EMAIL_EXIST(400_02, "邮箱已存在", "该邮箱已被注册"),
    PHONE_EXIST(400_03, "手机号已存在", "该手机号已被注册"),
    GENDER_UNKNOWN(400_04, "性别值不合法", "性别只能为 0(未知)、1(男)、2(女)"),
    USERNAME_BLANK(400_05, "用户名不能为空", "用户名字段必填且不可为空字符串"),
    NICKNAME_BLANK(400_07, "昵称不能为空", "昵称字段必填且不可为空字符串"),
    EMAIL_INVALID(400_06, "邮箱格式不正确", "邮箱不符合标准格式"),
    EMAIL_BLANK(400_11, "邮箱不能为空", "邮箱字段必填且不可为空字符串"),
    PHONE_INVALID(400_08, "手机号格式不正确", "手机号不符合规则（建议11位数字，以1开头）"),
    STATUS_DISABLED(400_09, "用户状态值不合法", "状态只能为 0(禁用)、1(正常)、2(锁定)"),
    BIRTHDAY_INVALID(400_10, "生日格式或值不合法", "生日日期无效或为未来时间"),
    PASSWORD_BLANK(400_12, "密码不能为空", "密码字段必填"),
    USER_NOT_FOUND(400_13, "用户不存在或密码错误", "根据账号未找到匹配用户或密码不正确"),
    USER_NOT_LOGIN(400_14, "请先登录", "当前请求需要登录状态"),
    NO_AUTHORITY(400_15, "无操作权限", "当前用户无权执行该操作"),
    ;

    UserCode(int code, String message, String description) {
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
