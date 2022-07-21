package com.guxt.take.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guxt.take.common.R;
import com.guxt.take.common.SystemConstant;
import com.guxt.take.common.WeixinProperties;
import com.guxt.take.entity.User;
import com.guxt.take.service.UserService;
import com.guxt.take.utils.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private RedisUtils redisUtils;

    @Autowired
    private WeixinProperties weixinProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private Environment environment;

    @Autowired
    private HttpClientUtil httpClientUtil;



    @PostMapping("/wxLogin")
    public R<HashMap<String, String>> wxLogin(@RequestBody User userInfo) throws Exception {
        System.out.println(userInfo.getCode());
        String jscode2sessionUrl = environment.getProperty("weixin.jscode2sessionUrl")  + "?appid=" + environment.getProperty("weixin.appid") + "&secret=" +environment.getProperty("weixin.secret") + "&js_code=" + userInfo.getCode() + "&grant_type=authorization_code";
        System.out.println(jscode2sessionUrl);
        String result = httpClientUtil.sendHttpGet(jscode2sessionUrl);
        System.out.println(result);
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.get("openid").toString();
        System.out.println(openid);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenid, openid);
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            userInfo.setOpenid(openid);
            userInfo.setCreateTime(LocalDateTime.now());
            userService.save(userInfo);
        } else {
            user.setNickName(userInfo.getNickName());
            user.setAvatarUrl(userInfo.getAvatarUrl());
            userService.updateById(user);

        }
        User one = userService.getOne(queryWrapper);
        String userId = one.getId().toString();
        String phone = one.getPhone();

        String token = JwtUtils.createJWT(userId, userInfo.getNickName(), SystemConstant.JWT_TTL);
        Claims claims = JwtUtils.parseJWT(token);
        String id = claims.getId();
        System.out.println("userId" + id);
        HashMap<String,String> map = new HashMap<>();
        map.put("token",token);
        map.put("phone",phone);

        return R.success(map);

    }




    @PostMapping("/loginout")
    public R<String> logout() {
        return userService.logout();
    }

    @GetMapping("/getUserInfo/{phone}")
    public R<User> getUserInfo(@PathVariable String phone) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(phone != null, User::getPhone, phone);
        User byId = userService.getOne(queryWrapper);
        if (byId != null) {
            return R.success(byId);
        }
        return R.error("用户信息查询失败！");

    }

    @PutMapping
    public R<String> updateUser(@RequestBody User user) {
        userService.updateUserById(user);
        return R.success("修改成功！");
    }

    @GetMapping("/getUserCount")
    public R<Long> getUserCount() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        long count = userService.count(queryWrapper);
        return R.success(count);
    }

    /**
     * 会员分页条件查询
     *
     * @param page
     * @param pageSize
     * @param phone
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String phone) {
        //构造分页构造器
        Page pageInfo = new Page<>(page, pageSize);
        return userService.pageQuery(pageInfo, phone);
    }

    /**
     * 根据ID修改会员信息
     *
     * @param user
     * @return
     */
    @PutMapping("/updateStatus")
    public R<String> update(@RequestBody User user) {
        userService.updateById(user);
        return R.success("会员信息修改成功！");
    }

    /**
     * 修改页面，根据ID回显数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            return R.success(user);
        }
        return R.error("没有查询到对应会员信息");
    }


}
