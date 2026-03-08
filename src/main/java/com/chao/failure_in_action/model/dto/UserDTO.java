package com.chao.failure_in_action.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

    // ========== 更新场景必填 ==========
    @NotNull(message = "用户ID不能为空", groups = Update.class)
    private Long id;

    // ========== 创建场景必填 ==========
    @NotBlank(message = "用户名不能为空", groups = Create.class)
    @Size(min = 4, max = 20, message = "用户名长度4-20个字符", groups = Create.class)
    private String username;

    @NotBlank(message = "密码不能为空", groups = Create.class)
    @Size(min = 6, max = 32, message = "密码长度6-32个字符", groups = Create.class)
    private String password;

    // ========== 公共字段（创建+更新都可选/有约束）==========
    @Size(max = 50, message = "昵称长度不能超过50个字符", groups = {Create.class, Update.class})
    private String nickname;

    @Email(message = "邮箱格式不正确", groups = {Create.class, Update.class})
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确", groups = {Create.class, Update.class})
    private String phone;

    @Min(value = 0, message = "性别参数错误", groups = {Create.class, Update.class})
    @Max(value = 2, message = "性别参数错误", groups = {Create.class, Update.class})
    private Integer gender;

    @Past(message = "生日必须是过去的日期", groups = {Create.class, Update.class})
    private Date birthday;

    // ========== 分组标记接口 ==========
    public interface Create {
    }

    public interface Update {
    }
}