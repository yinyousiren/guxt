package com.guxt.take.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import com.guxt.take.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    private HttpServletRequest request;

    /**
     * 插入操作，自动填充
     *
     * @param metaObject
     */
    @SneakyThrows
    @Override
    public void insertFill(MetaObject metaObject) {
        String token = request.getHeader("token");

        Claims claims = JwtUtils.parseJWT(token);
        Long id = Long.valueOf(claims.getId());


        if (metaObject.hasSetter("createTime")) {
            metaObject.setValue("createTime", LocalDateTime.now());
        }
        if (metaObject.hasSetter("updateTime")) {
            metaObject.setValue("updateTime", LocalDateTime.now());
        }

        if (metaObject.hasSetter("createUser")) {
            metaObject.setValue("createUser", id);
        }
        if (metaObject.hasSetter("updateUser")) {
            metaObject.setValue("updateUser", id);
        }


        if (metaObject.hasSetter("createUser")) {
            metaObject.setValue("createUser", id);
        }
        if (metaObject.hasSetter("updateUser")) {
            metaObject.setValue("updateUser", id);
        }


    }

    /**
     * 更新操作，自动填充
     *
     * @param metaObject
     */
    @SneakyThrows
    @Override
    public void updateFill(MetaObject metaObject) {
        String token = request.getHeader("token");

        Claims claims = JwtUtils.parseJWT(token);
        Long id = Long.valueOf(claims.getId());


        if (metaObject.hasSetter("createTime")) {
            metaObject.setValue("createTime", LocalDateTime.now());
        }

        if (metaObject.hasSetter("updateTime")) {
            metaObject.setValue("updateTime", LocalDateTime.now());
        }

        if (metaObject.hasSetter("createUser")) {
            metaObject.setValue("createUser", id);
        }

        if (metaObject.hasSetter("updateUser")) {
            metaObject.setValue("updateUser", id);
        }


        if (metaObject.hasSetter("createUser")) {
            metaObject.setValue("createUser", id);
        }
        if (metaObject.hasSetter("updateUser")) {
            metaObject.setValue("updateUser", id);
        }

    }
}
