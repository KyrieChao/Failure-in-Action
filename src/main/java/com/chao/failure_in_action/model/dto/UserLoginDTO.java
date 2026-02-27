package com.chao.failure_in_action.model.dto;

import lombok.Data;

/**
 * 用户登录
 * @author Chao
 * @Github <a href="https://github.com/KyrieChao/Failure">Failure</a>
 */
@Data
public class UserLoginDTO {
    private String email;
    private String password;
}