package com.chao.failure_in_action.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.failure_in_action.model.entity.User;
import com.chao.failure_in_action.service.UserService;
import com.chao.failure_in_action.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author dell
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2026-02-24 15:40:48
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




