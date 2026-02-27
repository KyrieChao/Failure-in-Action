package com.chao.failure_in_action.contant;

/**
 * 用户常量
 * @author Chao
 * @Github <a href="https://github.com/KyrieChao/Failure">Failure</a>
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";
    /**
     * 盐值，混淆密码
     */
    String SALT = "242702";

    int DEFAULT_STATUS = 1;
    //  ------- 权限 --------
    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;
}
