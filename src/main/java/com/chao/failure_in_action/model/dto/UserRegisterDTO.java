package com.chao.failure_in_action.model.dto;

import lombok.Data;

import java.util.Date;

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