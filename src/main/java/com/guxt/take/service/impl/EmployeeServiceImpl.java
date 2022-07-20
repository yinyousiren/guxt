package com.guxt.take.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.guxt.take.common.R;
import com.guxt.take.common.SystemConstant;
import com.guxt.take.entity.Employee;
import com.guxt.take.mapper.EmployeeMapper;
import com.guxt.take.service.EmployeeService;
import com.guxt.take.utils.EmpThreadLocal;
import com.guxt.take.utils.JwtUtils;
import com.guxt.take.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private RedisUtils redisUtils;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private HttpServletRequest httpServletRequest;


    @Override
    public R<HashMap<String, Object>> login(Employee employee, HttpServletRequest request) {

        //1.将页面提交的密码进行MD5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

        //2.根据页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeMapper.selectOne(queryWrapper);

        //3.如果没有查到则返回登录失败结果
        if (emp == null){
            return R.error("登录失败！");
        }
        //4.密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败！");
        }
        //5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0){
            return R.error("账号已禁用！");
        }

        String token = JwtUtils.createJWT(emp.getId().toString(), emp.getName(), SystemConstant.JWT_TTL);

        //redisUtils.saveEmployee2Redis(emp.getId().toString());

        HashMap<String,Object> map = new HashMap<>();
        map.put("emp",emp);
        map.put("token",token);

        return R.success(map);
    }

    @Override
    public R<String> logout(HttpServletRequest request) {
        //Long empId = EmpThreadLocal.get().getId();
        EmpThreadLocal.remove();
        return R.success("退出成功");
    }

    @Override
    public R<Page> pageQuery(Page pageInfo, String name) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeMapper.selectMapsPage(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    @Override
    public boolean save(Employee employee) {
        //设置默认密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        //
        //Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
        employeeMapper.insert(employee);
        return true;
    }

    @Override
    public boolean updateById(Employee employee) {
        //Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(empId);
        employeeMapper.updateById(employee);

        return true;
    }
}
