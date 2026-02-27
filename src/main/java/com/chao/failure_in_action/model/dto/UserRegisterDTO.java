package com.chao.failure_in_action.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * 用户注册
 * @author Chao
 * @Github <a href="https://github.com/KyrieChao/Failure">Failure</a>
 */
@Data
public class UserRegisterDTO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String password;
    private Integer gender;
    private Date birthday;
    private Integer status;
}