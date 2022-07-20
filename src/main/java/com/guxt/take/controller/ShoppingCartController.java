package com.guxt.take.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.guxt.take.common.R;
import com.guxt.take.entity.ShoppingCart;
import com.guxt.take.entity.User;
import com.guxt.take.service.ShoppingCartService;
import com.guxt.take.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping ("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        return shoppingCartService.add2shoppingCart(shoppingCart);
    }


    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long id = UserThreadLocal.get().getId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,id);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        Long id = UserThreadLocal.get().getId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,id);
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功！");
    }

    @PostMapping("/sub")
    public R<ShoppingCart> changeNumber(@RequestBody ShoppingCart shoppingCart){
        User userInfo = UserThreadLocal.get();
        Long userId = userInfo.getId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getDishId() != null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId() != null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if (one.getNumber() == 1){
            shoppingCartService.removeById(one);
        }
        Integer number = one.getNumber();
        one.setNumber(number-1);
        shoppingCartService.updateById(one);
        return R.success(one);

    }
}
