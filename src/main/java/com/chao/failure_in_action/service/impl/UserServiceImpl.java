package com.chao.failure_in_action.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.failfast.Failure;
import com.chao.failfast.annotation.Validate;
import com.chao.failure_in_action.mapper.UserMapper;
import com.chao.failure_in_action.model.dto.UserDeleteDTO;
import com.chao.failure_in_action.model.dto.UserLoginDTO;
import com.chao.failure_in_action.model.dto.UserRegisterDTO;
import com.chao.failure_in_action.model.dto.UserUpdateDTO;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.model.enums.RequestCode;
import com.chao.failure_in_action.model.enums.UserCode;
import com.chao.failure_in_action.service.UserService;
import com.chao.failure_in_action.validator.CustomValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.chao.failure_in_action.contant.UserConstant.*;

/**
 * @author Chao
 * @date 2026/2/24 15:49
 * @Github <a href="https://github.com/KyrieChao/Failure">Failure</a>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 使用自定义验证器进行登录验证，关闭快速验证模式
     *
     * @param dto     用户登录数据传输对象，包含登录信息
     * @param request HTTP请求对象，用于获取会话
     * @return 返回去除敏感信息后的安全用户对象
     */
    @Override
    @Validate(value = CustomValidator.class, fast = false)  // 使用自定义验证器，关闭快速验证
    public User login(UserLoginDTO dto, HttpServletRequest request) {
        User user = getOne(new QueryWrapper<User>().eq("email", dto.getEmail()));
        User safetyUser = getSafetyUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 用户注册方法
     *
     * @param dto 注册信息
     * @return 是否注册成功
     */
    @Override
    @Validate(value = CustomValidator.class, fast = false) // 使用自定义验证器进行验证，fast=false表示不使用快速验证模式
    public boolean register(UserRegisterDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setGender(dto.getGender());
        // 密码加密 我就偷懒了 推荐使用 BCrypt / Argon2
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + dto.getPassword()).getBytes());
        user.setPassword(encryptPassword);
        user.setBirthday(dto.getBirthday());
        user.setStatus(DEFAULT_STATUS);
        user.setRole(DEFAULT_ROLE);
        return save(user);
    }

    /**
     * 获取当前登录用户的信息
     *
     * @param request HTTP请求对象，用于获取会话信息
     * @return 返回处理后的安全用户信息
     */
    @Override
    public User currentUserInfo(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        Failure.begin()
                .notNull(currentUser, UserCode.USER_NOT_LOGIN)
                .fail();
        long userId = currentUser.getId();
        User user = this.getById(userId);
        return this.getSafetyUser(user);
    }

    /**
     * 根据用户名搜索用户列表
     *
     * @param username 要搜索的用户名
     * @param request  HTTP请求对象，用于获取当前用户信息
     * @return 返回处理后的用户列表，包含安全信息
     */
    @Override
    public List<User> searchUsers(String username, HttpServletRequest request) {
        Failure.begin()
                .state(isNotAdmin(request), UserCode.NO_AUTHORITY)
                .notBlank(username, UserCode.USERNAME_BLANK)
                .fail();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = this.list(queryWrapper);
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 用户登出方法
     *
     * @param request HttpServletRequest对象，包含当前请求的信息
     * @return 返回boolean类型，表示登出操作是否成功
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        Failure.begin()
                .notNull(request, RequestCode.REQUEST_OBJECT_NOT_NULL)
                .fail();
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 根据用户ID删除用户信息
     *
     * @param dto     包含用户删除信息的DTO对象，其中包含要删除的用户ID
     * @param request HTTP请求对象，用于获取当前用户信息
     * @return 删除操作是否成功，true表示成功，false表示失败
     */
    @Override
    public boolean deleteUser(UserDeleteDTO dto, HttpServletRequest request) {
        Failure.begin()
                .state(isNotAdmin(request), UserCode.NO_AUTHORITY)
                .positive(dto.getId(), UserCode.USER_NOT_FOUND)
                .fail();
        return this.removeById(dto.getId());
    }

    /**
     * 获取安全的用户信息，去除敏感信息
     *
     * @param originUser 原始用户对象
     * @return 返回一个新的用户对象，只包含非敏感信息
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) return null;
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());                 // 用户ID
        safetyUser.setUsername(originUser.getUsername());     // 用户名
        safetyUser.setNickname(originUser.getNickname());     // 昵称
        safetyUser.setEmail(originUser.getEmail());           // 邮箱
        safetyUser.setPhone(originUser.getPhone());           // 电话
        safetyUser.setGender(originUser.getGender());         // 性别
        safetyUser.setBirthday(originUser.getBirthday());     // 生日
        safetyUser.setStatus(originUser.getStatus());         // 状态
        safetyUser.setRole(originUser.getRole());             // 角色
        safetyUser.setIsDeleted(originUser.getIsDeleted());   // 是否删除
        safetyUser.setCreateTime(originUser.getCreateTime()); // 创建时间
        safetyUser.setUpdateTime(originUser.getUpdateTime()); // 更新时间
        return safetyUser;
    }


    /**
     * 更新用户信息方法
     *
     * @param dto     包含更新后用户信息的DTO对象
     * @param request HTTP请求对象，用于获取当前登录用户信息
     * @return 更新成功返回true，否则返回false
     */
    @Override
    public boolean updateUser(UserUpdateDTO dto, HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        Failure.begin()
                .notNull(currentUser, UserCode.USER_NOT_LOGIN)
                .fail();
        long userId = currentUser.getId();
        // 校验请求更新的用户ID与当前登录用户ID是否一致，防止越权操作
        Failure.begin()
                .state(dto.getId() == userId, UserCode.USER_NOT_FOUND)
                .fail();
        User user = new User();
        user.setId(userId);
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());
        user.setBirthday(dto.getBirthday());
        return this.updateById(user);
    }


    /**
     * 管理员角色判断
     */
    private boolean isNotAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user == null || user.getRole() != ADMIN_ROLE;
    }
}




