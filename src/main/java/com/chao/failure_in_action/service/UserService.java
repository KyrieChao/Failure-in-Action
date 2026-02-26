package com.chao.failure_in_action.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chao.failure_in_action.model.dto.UserDeleteDTO;
import com.chao.failure_in_action.model.dto.UserLoginDTO;
import com.chao.failure_in_action.model.dto.UserRegisterDTO;
import com.chao.failure_in_action.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author dell
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2026-02-24 15:40:48
 */
public interface UserService extends IService<User> {

    boolean register(UserRegisterDTO dto);

    User login(UserLoginDTO dto, HttpServletRequest request);

    User currentUserInfo(HttpServletRequest request);

    List<User> searchUsers(String username, HttpServletRequest request);

    boolean userLogout(HttpServletRequest request);

    boolean deleteUser(UserDeleteDTO dto, HttpServletRequest request);

    User getSafetyUser(User originUser);
}
