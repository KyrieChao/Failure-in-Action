package com.chao.failure_in_action.validator;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.chao.failfast.Failure;
import com.chao.failfast.validator.TypedValidator;
import com.chao.failure_in_action.model.dto.UserDTO;
import com.chao.failure_in_action.model.dto.UserLoginDTO;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.model.enums.UserCode;
import com.chao.failure_in_action.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

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
    @Resource
    private UserService userService;

    @Override
    protected void registerValidators() {
        // 登录校验
        register(UserLoginDTO.class, this::validateLogin);

        // 注册校验
        register(UserDTO.class, this::validateRegister);
    }

    private void validateLogin(UserLoginDTO dto, ValidationContext ctx) {
        // 1. 格式校验
        Failure.with(ctx)
                .notBlank(dto.getPassword(), UserCode.PASSWORD_BLANK)
                .email(dto.getEmail(), UserCode.EMAIL_INVALID)
                .verify();
        // 登录参数校验 如有错误，则直接返回
        if (ctx.isFailed()) return;

        // 2. 业务校验（数据库）
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + dto.getPassword()).getBytes());
        boolean exists = userService.lambdaQuery()
                .eq(User::getEmail, dto.getEmail())
                .eq(User::getPassword, encryptPassword)
                .exists();

        Failure.with(ctx)
                .state(exists, UserCode.USER_NOT_FOUND)
                .verify();
    }

    private void validateRegister(UserDTO dto, ValidationContext context) {
        // 只校验数据库唯一性，字段格式由 @Validated 处理
        Failure.with(context)
                .isFalse(exists(User::getUsername, dto.getUsername()), UserCode.USERNAME_EXIST)
                .isFalse(exists(User::getEmail, dto.getEmail()), UserCode.EMAIL_EXIST)
                .isFalse(exists(User::getPhone, dto.getPhone()), UserCode.PHONE_EXIST)
                .verify();
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
        // 支持多个类型，TypedValidator 内部会处理不同类型的校验
        return Object.class;
    }
}
