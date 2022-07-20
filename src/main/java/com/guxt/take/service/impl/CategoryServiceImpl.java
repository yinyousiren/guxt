package com.guxt.take.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxt.take.common.R;
import com.guxt.take.entity.Category;
import com.guxt.take.entity.Dish;
import com.guxt.take.entity.Setmeal;
import com.guxt.take.excption.CustomException;
import com.guxt.take.mapper.CategoryMapper;
import com.guxt.take.mapper.DishMapper;
import com.guxt.take.mapper.SetmealMapper;
import com.guxt.take.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;
    @Override
    public R<Page> pageQuery(Page pageInfo) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        categoryMapper.selectPage(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @Override
    public void delete(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        Long dishCount = dishMapper.selectCount(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联了，抛出一个异常
        if (dishCount > 0){
            throw new CustomException("当前分类下关联了菜品，不能删除");

        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        Long setmealCount = setmealMapper.selectCount(setmealLambdaQueryWrapper);
        if (setmealCount > 0){
            throw new CustomException("当前分类下关联了套餐，不能删除");

        }
        categoryMapper.deleteById(id);
        return;
    }

    @Override
    public R<List<Category>> findCategoryByQuery(Category category) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        categoryLambdaQueryWrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categories = categoryMapper.selectList(categoryLambdaQueryWrapper);
        return R.success(categories);
    }


}
