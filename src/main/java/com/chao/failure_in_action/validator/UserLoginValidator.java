package com.chao.failure_in_action.validator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chao.failfast.Failure;
import com.chao.failfast.annotation.FastValidator;
import com.chao.failure_in_action.mapper.UserMapper;
import com.chao.failure_in_action.model.dto.UserLoginDTO;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.model.enums.UserCode;
import jakarta.annotation.Resource;
import org.springframework.util.DigestUtils;

import static com.chao.failure_in_action.contant.UserConstant.SALT;

public class UserLoginValidator implements FastValidator<UserLoginDTO> {
    @Resource
    private UserMapper userMapper;

    @Override
    public void validate(UserLoginDTO dto, ValidationContext context) {
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + dto.getPassword()).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", dto.getEmail());
        queryWrapper.eq("passwordHash", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        Failure.with(context)
                .notBlank(dto.getEmail(), UserCode.EMAIL_BLANK)
                .notBlank(dto.getPassword(), UserCode.PASSWORD_BLANK)
                .state(user != null, UserCode.USER_NOT_FOUND)
                .verify();
    }
}
