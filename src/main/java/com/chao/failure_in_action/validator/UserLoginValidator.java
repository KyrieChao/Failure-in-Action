package com.chao.failure_in_action.validator;
/*
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chao.failfast.Failure;
import com.chao.failfast.annotation.FastValidator;
import com.chao.failure_in_action.model.dto.UserLoginDTO;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.model.enums.UserCode;
import com.chao.failure_in_action.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import static com.chao.failure_in_action.contant.UserConstant.SALT;

@Component
public class UserLoginValidator implements FastValidator<UserLoginDTO> {
    @Resource
    private UserService userService;

    @Override
    public void validate(UserLoginDTO dto, ValidationContext context) {
        Failure.with(context)
                .notBlank(dto.getEmail(), UserCode.EMAIL_BLANK)
                .notBlank(dto.getPassword(), UserCode.PASSWORD_BLANK)
                .email(dto.getEmail(), UserCode.EMAIL_INVALID)
                .verify();
        if (context.isFailed()) {
            return;
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + dto.getPassword()).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", dto.getEmail());
        queryWrapper.eq("password", encryptPassword);
        User user = userService.getOne(queryWrapper);
        Failure.with(context)
                .state(user != null, UserCode.USER_NOT_FOUND)
                .verify();
    }

    @Override
    public Class<?> getSupportedType() {
        return UserLoginDTO.class;
    }
}*/
