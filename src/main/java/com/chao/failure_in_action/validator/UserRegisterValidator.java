package com.chao.failure_in_action.validator;


import com.chao.failfast.annotation.FastValidator;
import com.chao.failure_in_action.model.dto.UserDTO;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.model.enums.UserCode;
import com.chao.failure_in_action.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements FastValidator<UserDTO> {

    @Resource
    private UserService userService;

    @Override
    public void validate(UserDTO dto, ValidationContext context) {
        User one = userService.lambdaQuery()
                .eq(User::getUsername, dto.getUsername())
                .or()
                .eq(User::getEmail, dto.getEmail())
                .one();
        if (one != null) context.reportError(UserCode.USERNAME_EXIST);
    }

    @Override
    public Class<?> getSupportedType() {
        return UserDTO.class;
    }
}
