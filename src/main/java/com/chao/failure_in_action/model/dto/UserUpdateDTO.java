package com.chao.failure_in_action.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

/**
 * 用户更新
 * 演示 fast = false 的用法 才把UserUpdateDTO写成这样
 * @author Chao
 * @Github <a href="https://github.com/KyrieChao/Failure">Failure</a>
 */
@Data
public class UserUpdateDTO {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Min(value = 0, message = "性别参数错误")
    @Max(value = 2, message = "性别参数错误")
    private Integer gender;

    @Past(message = "生日必须是过去的日期")
    private Date birthday;
}