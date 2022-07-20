package com.guxt.take.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guxt.take.common.R;
import com.guxt.take.entity.Employee;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface EmployeeService extends IService<Employee> {

    /**
     * 登录
     * @param employee
     * @param request
     * @return
     */
    R<HashMap<String, Object>> login(Employee employee, HttpServletRequest request);

    /**
     * 退出
     * @param request
     * @return
     */
    R<String> logout(HttpServletRequest request);

    /**
     * 分页查询
     * @param pageInfo
     * @param name
     * @return
     */
    R<Page> pageQuery(Page pageInfo, String name);



}
