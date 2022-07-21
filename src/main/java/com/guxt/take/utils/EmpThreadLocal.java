package com.guxt.take.utils;

import com.guxt.take.entity.Employee;

/**
 * 存储管理员Id
 */

public class EmpThreadLocal {
    private EmpThreadLocal(){}

    private static final ThreadLocal<Employee> LOCAL = new ThreadLocal<>();

    public static void put(Employee Employee){
        LOCAL.set(Employee);
    }
    public static Employee get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}
