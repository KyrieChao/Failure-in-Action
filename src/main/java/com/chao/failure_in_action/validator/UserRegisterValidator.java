package com.chao.failure_in_action.validator;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.chao.failfast.Failure;
import com.chao.failfast.annotation.FastValidator;
import com.chao.failure_in_action.model.dto.UserDTO;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.model.enums.UserCode;
import com.chao.failure_in_action.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author Chao
 * FastValidator 方式校验示例
 * @Github <a href="https://github.com/KyrieChao/Failure">Failure</a>
 */
@Component
public class UserRegisterValidator implements FastValidator<UserDTO> {

    @Resource
    private UserService userService;

    /**
     * 验证用户数据传输对象(UserDTO)的唯一性
     * 该方法主要负责检查用户名、邮箱和手机号是否已存在于数据库中
     * 字段的格式验证由@Validated注解处理，本方法不负责格式校验
     *
     * @param dto 需要验证的用户数据传输对象
     * @param context 验证上下文，用于构建和返回验证失败信息
     */
    @Override
    public void validate(UserDTO dto, ValidationContext context) {
        Failure.with(context)
                // 检查用户名是否已存在，如果存在则返回用户名已存在的错误码
                .isFalse(exists(User::getUsername, dto.getUsername()), UserCode.USERNAME_EXIST)
                // 检查邮箱是否已存在，如果存在则返回邮箱已存在的错误码
                .isFalse(exists(User::getEmail, dto.getEmail()), UserCode.EMAIL_EXIST)
                // 检查手机号是否已存在，如果存在则返回手机号已存在的错误码
                .isFalse(exists(User::getPhone, dto.getPhone()), UserCode.PHONE_EXIST)
                // 执行验证，如果有任何一项校验失败，则返回对应的错误信息
                .verify();
    }

    /**
     * 检查指定列的值是否已存在于数据库中
     *
     * @param column User表的列，通过SFunction<User, ?>指定
     * @param value  要检查的值
     * @return 如果值存在且未被删除则返回true，否则返回false
     */
    private boolean exists(SFunction<User, ?> column, Object value) {
        if (value == null) return false;
        return userService.lambdaQuery()
                .eq(column, value)
                .eq(User::getIsDeleted, 0)
                .exists();
    }

    /**
     * 获取当前处理器支持的类型
     * 该方法用于标识此处理器能够处理的数据类型
     *
     * @return 返回处理器支持的类型，这里是UserDTO类
     */
    @Override
    public Class<?> getSupportedType() {
        return UserDTO.class;
    }
}

