package com.guxt.take.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxt.take.entity.SetmealDish;
import com.guxt.take.mapper.CategoryMapper;
import com.guxt.take.mapper.SetmealDishMapper;
import com.guxt.take.mapper.SetmealMapper;
import com.guxt.take.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Slf4j
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private CategoryMapper categoryMapper;


}
