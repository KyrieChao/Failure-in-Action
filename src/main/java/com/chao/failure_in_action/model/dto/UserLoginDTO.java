package com.chao.failure_in_action.model.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}