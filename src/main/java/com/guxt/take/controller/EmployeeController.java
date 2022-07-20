package com.guxt.take.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guxt.take.common.R;
import com.guxt.take.entity.Employee;
import com.guxt.take.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<HashMap<String, Object>> login(@RequestBody Employee employee, HttpServletRequest request){

        return employeeService.login(employee,request);
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        return employeeService.logout(request);
    }


    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> sava(@RequestBody Employee employee){

        employeeService.save(employee);
        return R.success("新增员工成功！");
    }

    /**
     * 员工分页条件查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
         Page pageInfo = new Page<>(page,pageSize);
         return employeeService.pageQuery(pageInfo,name);
    }

    /**
     * 根据ID修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success("员工信息修改成功！");
    }

    /**
     * 修改页面，根据ID回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }

    @GetMapping("/getEmpCount")
    public R<Long> getEmpCount(){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        long count = employeeService.count(queryWrapper);
        return R.success(count);
    }





}
