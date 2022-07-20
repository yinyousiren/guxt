package com.guxt.take.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guxt.take.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
