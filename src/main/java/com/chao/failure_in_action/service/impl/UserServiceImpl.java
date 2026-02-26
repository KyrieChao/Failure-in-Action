package com.chao.failure_in_action.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.failfast.Failure;
import com.chao.failfast.annotation.Validate;
import com.chao.failure_in_action.mapper.UserMapper;
import com.chao.failure_in_action.model.dto.UserDeleteDTO;
import com.chao.failure_in_action.model.dto.UserLoginDTO;
import com.chao.failure_in_action.model.dto.UserRegisterDTO;
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
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    @Validate(value = CustomValidator.class, fast = false)
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
        user.setStatus(dto.getStatus());
        return save(user);
    }

    @Validate(value = CustomValidator.class, fast = false)
    @Override
    public User login(UserLoginDTO dto, HttpServletRequest request) {
        User user = getOne(new QueryWrapper<User>().eq("email", dto.getEmail()));
        User safetyUser = getSafetyUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

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

    @Override
    public boolean userLogout(HttpServletRequest request) {
        Failure.begin()
                .notNull(request, RequestCode.REQUEST_OBJECT_NOT_NULL)
                .fail();
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public boolean deleteUser(UserDeleteDTO dto, HttpServletRequest request) {
        Failure.begin()
                .state(isNotAdmin(request), UserCode.NO_AUTHORITY)
                .positive(dto.getId(), UserCode.USER_NOT_FOUND)
                .fail();
        return this.removeById(dto.getId());
    }

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) return null;
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setNickname(originUser.getNickname());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setBirthday(originUser.getBirthday());
        safetyUser.setStatus(originUser.getStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        return safetyUser;
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




