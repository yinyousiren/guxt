package com.guxt.take.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxt.take.common.R;
import com.guxt.take.entity.ShoppingCart;
import com.guxt.take.entity.User;
import com.guxt.take.mapper.ShoppingCartMapper;
import com.guxt.take.service.ShoppingCartService;
import com.guxt.take.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private HttpServletRequest request;

    @Override
    public R<ShoppingCart> add2shoppingCart(ShoppingCart shoppingCart) {
        //Long userId = (Long) request.getSession().getAttribute("user");
        User userInfo = UserThreadLocal.get();
        Long userId = userInfo.getId();
        shoppingCart.setUserId(userId);
        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);

        if (dishId != null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCart1 = shoppingCartMapper.selectOne(queryWrapper);


        //如果已经存在，就在原来的基础上+1
        if (shoppingCart1 != null){
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number+1);
            shoppingCartMapper.updateById(shoppingCart1);
        }else {
            //如果不存在，则添加到购物车，数量默认就是1
            shoppingCart.setNumber(1);
            shoppingCartMapper.insert(shoppingCart);
            shoppingCart1 = shoppingCart;
        }
        return R.success(shoppingCart1);
    }
}
