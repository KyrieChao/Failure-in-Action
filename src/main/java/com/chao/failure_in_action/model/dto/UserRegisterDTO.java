package com.chao.failure_in_action.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private Long id;
    @NotBlank
    private String username;
    @NotNull
    private String nickname;
    private String email;
    private String phone;
    private String passwordHash;
    private Integer gender;
    private Date birthday;
    private Integer status;
}