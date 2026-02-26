package com.chao.failure_in_action.controller;

import com.chao.failfast.annotation.Validate;
import com.chao.failfast.result.Result;
import com.chao.failure_in_action.model.dto.UserDeleteDTO;
import com.chao.failure_in_action.model.dto.UserLoginDTO;
import com.chao.failure_in_action.model.dto.UserRegisterDTO;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

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

    @PostMapping("/logout")
    public Result<Boolean> userLogout(HttpServletRequest request) {
        boolean result = userService.userLogout(request);
        return Result.ok(result);
    }

    @GetMapping("/current")
    public Result<User> getCurrentUser(HttpServletRequest request) {
        User safetyUser = userService.currentUserInfo(request);
        return Result.ok(safetyUser);
    }


    @GetMapping("/search")
    public Result<List<User>> searchUsers(String username, HttpServletRequest request) {
        return Result.ok(userService.searchUsers(username, request));
    }

    @PostMapping("/delete")
    public Result<Boolean> deleteUser(@RequestBody UserDeleteDTO dto, HttpServletRequest request) {
        return Result.ok(userService.deleteUser(dto, request));
    }
}
