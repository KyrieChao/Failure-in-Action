package com.chao.failure_in_action.controller;

import com.chao.failfast.annotation.Validate;
import com.chao.failfast.result.Result;
import com.chao.failure_in_action.model.dto.UserLoginDTO;
import com.chao.failure_in_action.model.dto.UserRegisterDTO;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {

    private final UserService userService;

    @PostMapping("/register")
    @Validate(fast = false)
    public Result<Boolean> register(@RequestBody @Valid UserRegisterDTO dto) {
        boolean b = userService.register(dto);
        return Result.ok(b);
    }


    @PostMapping("/login")
    public Result<User> login(@RequestBody UserLoginDTO dto, HttpServletRequest request) {
        User user = userService.login(dto, request);
        return Result.ok(user);
    }
}
