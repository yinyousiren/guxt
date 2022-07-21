package com.guxt.take.utils;


import com.guxt.take.entity.User;

/**
 * 存储用户Id
 */

public class UserThreadLocal {
    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();
    private UserThreadLocal(){

    }
    public static void put(User user){
        LOCAL.set(user);
    }
    public static User get(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }

}
