package com.guxt.take.handler;

import com.alibaba.fastjson.JSON;
import com.guxt.take.common.R;
import com.guxt.take.entity.Employee;
import com.guxt.take.entity.User;
import com.guxt.take.service.EmployeeService;
import com.guxt.take.service.UserService;
import com.guxt.take.utils.EmpThreadLocal;
import com.guxt.take.utils.JwtUtils;
import com.guxt.take.utils.RedisUtils;
import com.guxt.take.utils.UserThreadLocal;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

//@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private EmployeeService employeeService;

    @Resource
    private RedisUtils redisUtils;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    private List<String> urls = new ArrayList<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        System.out.println("token---->" + token);
        if (handler instanceof HandlerMethod) {

            if (StringUtils.isEmpty(token)) {
                response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
                return false;


            } else {
                Claims claims = JwtUtils.validateJWT(token).getClaims();
                if (claims == null) {
                    response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
                    return false;

                } else {
                    String id = JwtUtils.parseJWT(token).getId();
                    System.out.println(id+"id");
                    User byId = this.userService.getById(id);
                    Employee employee = this.employeeService.getById(id);
                    if (byId!=null){
                        UserThreadLocal.put(byId);
                        return true;
                    }
                    if (employee !=null){
                        EmpThreadLocal.put(employee);
                        return true;
                    }
                    return false;

                }
            }
        } else {
            return true;
        }
    }

    public List<String> getUrls(){

        urls.add("/employee/login");
        urls.add("/employee/logout");
        urls.add("/backend/**");
        urls.add("/front/**");
        urls.add("/user/sendMsg");
        urls.add("/user/login");
        urls.add("/user/wxLogin");


        return urls;
    }
}
