package com.chao.failure_in_action.validator;


/*
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.chao.failfast.Failure;
import com.chao.failfast.annotation.FastValidator;
import com.chao.failure_in_action.model.dto.UserRegisterDTO;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.model.enums.UserCode;
import com.chao.failure_in_action.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserRegisterValidator implements FastValidator<UserRegisterDTO> {
    private static final Set<Integer> GENDER_STATUS = Set.of(0, 1, 2);

    @Resource
    private UserService userService;

    @Override
    public void validate(UserRegisterDTO dto, ValidationContext context) {
        Failure.with(context)
                .satisfies(dto.getGender(), GENDER_STATUS::contains, UserCode.GENDER_UNKNOWN)
                .satisfies(dto.getStatus(), GENDER_STATUS::contains, UserCode.STATUS_DISABLED)
                .notBlank(dto.getUsername(), UserCode.USERNAME_BLANK)
                .notBlank(dto.getNickname(), UserCode.NICKNAME_BLANK)
                .email(dto.getEmail(), UserCode.EMAIL_INVALID)
                .mobile(dto.getPhone(), UserCode.PHONE_INVALID)
                .verify();
        checkDuplicate(dto, context);
    }

    private void checkDuplicate(UserRegisterDTO dto, ValidationContext context) {
        if (exists(User::getUsername, dto.getUsername())) {
            context.reportError(UserCode.USERNAME_EXIST);
        }
        if (exists(User::getEmail, dto.getEmail())) {
            context.reportError(UserCode.EMAIL_EXIST);
        }
        if (exists(User::getPhone, dto.getPhone())) {
            context.reportError(UserCode.PHONE_EXIST);
        }
    }

    private boolean exists(SFunction<User, ?> column, Object value) {
        if (value == null) return false;
        return userService.lambdaQuery()
                .eq(column, value)
                .eq(User::getIsDeleted, 0)
                .exists();
    }

    @Override
    public Class<?> getSupportedType() {
        return UserRegisterDTO.class;
    }
}
*/
