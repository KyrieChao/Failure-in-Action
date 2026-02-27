package com.chao.failure_in_action.validator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.chao.failfast.Failure;
import com.chao.failfast.validator.TypedValidator;
import com.chao.failure_in_action.model.dto.UserLoginDTO;
import com.chao.failure_in_action.model.dto.UserRegisterDTO;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.model.enums.UserCode;
import com.chao.failure_in_action.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Set;

import static com.chao.failure_in_action.contant.UserConstant.SALT;

/**
 * @author Chao
 * 统一校验注册器（TypedValidator 方式）
 * <p>
 * 本项目同时演示三种校验写法：
 * 1. 单独 FastValidator 类（如 UserRegisterValidator.java）
 * 2. 统一 TypedValidator 注册（本类）
 * 3. Service 层直接用 Failure 链式调用（早期版本示例）
 * <p>
 * 推荐生产环境统一使用一种方式（建议 TypedValidator 或单独 FastValidator）
 * @Github <a href="https://github.com/KyrieChao/Failure">Failure</a>
 */
@Component
public class CustomValidator extends TypedValidator {
    private static final Set<Integer> GENDER_STATUS = Set.of(0, 1, 2);
    @Resource
    private UserService userService;

    @Override
    protected void registerValidators() {
        // 登录校验
        register(UserLoginDTO.class, (dto, ctx) -> {
            Failure.with(ctx)
                    .notBlank(dto.getPassword(), UserCode.PASSWORD_BLANK)
                    .email(dto.getEmail(), UserCode.EMAIL_INVALID)
                    .verify();
            if (ctx.isFailed()) {
                return;
            }
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + dto.getPassword()).getBytes());
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", dto.getEmail());
            queryWrapper.eq("password", encryptPassword);
            User user = userService.getOne(queryWrapper);
            Failure.with(ctx)
                    .state(user != null, UserCode.USER_NOT_FOUND)
                    .verify();
        });

        // 注册校验
        register(UserRegisterDTO.class, (dto, ctx) -> {
            Failure.with(ctx)
                    .satisfies(dto.getGender(), GENDER_STATUS::contains, UserCode.GENDER_UNKNOWN,"性别")
                    .notBlank(dto.getUsername(), UserCode.USERNAME_BLANK)
                    .notBlank(dto.getNickname(), UserCode.NICKNAME_BLANK)
                    .email(dto.getEmail(), UserCode.EMAIL_INVALID)
                    .mobile(dto.getPhone(), UserCode.PHONE_INVALID)
                    .verify();
            if (ctx.isFailed()) {
                return;
            }
            checkDuplicate(dto, ctx);
        });
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
        return super.getSupportedType();
    }
}
