package com.chao.failure_in_action.controller;

import com.chao.failfast.annotation.Validate;
import com.chao.failfast.result.Result;
import com.chao.failure_in_action.model.dto.UserDTO;
import com.chao.failure_in_action.model.dto.UserDeleteDTO;
import com.chao.failure_in_action.model.dto.UserLoginDTO;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chao
 * @Github <a href="https://github.com/KyrieChao/Failure">Failure</a>
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 注册
     */
    @PostMapping("/register")
    @Validate(fast = false)
    public Result<Boolean> register(@RequestBody @Validated(UserDTO.Create.class) UserDTO dto) {
        boolean b = userService.register(dto);
        return Result.ok(b);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody UserLoginDTO dto, HttpServletRequest request) {
        User user = userService.login(dto, request);
        return Result.ok(user);
    }

    /**
     * 修改用户信息 用户只能修改自己的信息
     */
    @PostMapping("/update")
    @Validate(fast = false)
    // fast = false 开启收集失败模式
    public Result<Boolean> updateUser(@RequestBody @Validated(UserDTO.Update.class) UserDTO dto, HttpServletRequest request) {
        boolean result = userService.updateUser(dto, request);
        return Result.ok(result);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result<Boolean> userLogout(HttpServletRequest request) {
        boolean result = userService.userLogout(request);
        return Result.ok(result);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public Result<User> getCurrentUser(HttpServletRequest request) {
        User safetyUser = userService.currentUserInfo(request);
        return Result.ok(safetyUser);
    }

    /**
     * 搜索用户
     */
    @GetMapping("/search")
    public Result<List<User>> searchUsers(String username, HttpServletRequest request) {
        return Result.ok(userService.searchUsers(username, request));
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    public Result<Boolean> deleteUser(@RequestBody UserDeleteDTO dto, HttpServletRequest request) {
        return Result.ok(userService.deleteUser(dto, request));
    }
}
