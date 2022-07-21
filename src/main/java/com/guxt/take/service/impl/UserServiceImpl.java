package com.guxt.take.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxt.take.common.R;
import com.guxt.take.entity.User;
import com.guxt.take.mapper.UserMapper;
import com.guxt.take.service.UserService;
import com.guxt.take.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private RedisUtils redisUtils;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;


    @Override
    public R<String> logout() {

        Long userId = (Long)request.getSession().getAttribute("user");
        redisUtils.deleteUserFromRedis(userId.toString());
        request.getSession().removeAttribute("user");
        return R.success("退出成功！");
    }

    @Override
    public void updateUserById(User user) {
        userMapper.updateById(user);
        String avatar = user.getAvatarUrl();
        redisUtils.save2Db(avatar);
    }

    @Override
    public R<Page> pageQuery(Page pageInfo, String phone) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(phone),User::getPhone,phone);
        queryWrapper.orderByDesc(User::getCreateTime);
        userMapper.selectMapsPage(pageInfo,queryWrapper);
        return R.success(pageInfo);

    }

}
