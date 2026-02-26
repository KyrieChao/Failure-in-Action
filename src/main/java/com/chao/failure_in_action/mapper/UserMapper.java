package com.chao.failure_in_action.mapper;

import com.chao.failure_in_action.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author dell
 * @description 针对表【user(用户表)】的数据库操作Mapper
 * @createDate 2026-02-24 15:40:48
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




