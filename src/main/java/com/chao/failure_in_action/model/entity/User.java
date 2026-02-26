package com.chao.failure_in_action.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱（唯一）
     */
    private String email;

    /**
     * 手机号（唯一）
     */
    private String phone;

    /**
     * 密码哈希（bcrypt/argon2）
     */
    private String password;

    /**
     * 性别：0未知 1男 2女
     */
    private Integer gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 状态：0禁用 1正常 2锁定
     */
    private Integer status;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer role;

    /**
     * 逻辑删除：0正常 1已删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}