package com.guxt.take.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.guxt.take.common.R;
import com.guxt.take.dto.DishDto;
import com.guxt.take.entity.Category;
import com.guxt.take.entity.Dish;
import com.guxt.take.entity.DishFlavor;
import com.guxt.take.entity.SetmealDish;
import com.guxt.take.excption.CustomException;
import com.guxt.take.mapper.CategoryMapper;
import com.guxt.take.mapper.DishFlavorMapper;
import com.guxt.take.mapper.DishMapper;
import com.guxt.take.mapper.SetmealDishMapper;
import com.guxt.take.service.DishService;
import com.guxt.take.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Resource
    private RedisUtils redisUtils;


    @Override
    public R<Page> pageQuery(Page pageInfo, String name) {
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishMapper.selectPage(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish record : records) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(record,dishDto);
            Long categoryId = record.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            dishDtoList.add(dishDto);
        }
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "DishCache",allEntries = true)
    public void saveWithFlavor(DishDto dishDto) {

        dishMapper.insert(dishDto);
        Long dishId = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
            dishFlavorMapper.insert(flavor);
        }

        //flavors = flavors.stream().map((item) ->{
        //    item.setDishId(dishId);
        //    return item;
        //}).collect(Collectors.toList());
        //dishFlavorService.saveBatch(flavors);
        String image = dishDto.getImage();
        redisUtils.save2Db(image);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "DishCache",allEntries = true)
    public void updateWithFlavor(DishDto dishDto) {
        dishMapper.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorMapper.delete(queryWrapper);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
            dishFlavorMapper.insert(flavor);
        }
        String image = dishDto.getImage();
        redisUtils.save2Db(image);

    }

    @Override
    @CacheEvict(value = "DishCache",allEntries = true)
    public void updateStatusById(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getId,id);
        Dish dish = dishMapper.selectById(id);
        if (dish.getStatus() == 0){
                dish.setStatus(1);
            }else {
                dish.setStatus(0);
            }
            dishMapper.updateById(dish);

    }
    //@Override
    //public R<List<Dish>> findDishByCategoryId(Dish dish) {
    //
    //    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    //    queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
    //    queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
    //    List<Dish> dishes = dishMapper.selectList(queryWrapper);
    //    return R.success(dishes);
    //}

    @Override
    @Cacheable(value = "DishCache",key = "#dish.categoryId+'_'+#dish.status")
    public R<List<DishDto>> findDishByCategoryId(Dish dish) {
        List<DishDto> dishDtoList =null;
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishMapper.selectList(queryWrapper);
        dishDtoList = new ArrayList<>();

        for (Dish dish1 : dishes) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1,dishDto);
            Long categoryId = dish1.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            if (category != null){
                String name = category.getName();
                dishDto.setCategoryName(name);
            }

            //当前菜品ID
            Long dish1Id = dish1.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dish1Id);
            List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            dishDtoList.add(dishDto);

        }


        return R.success(dishDtoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "DishCache",allEntries = true)
    public void delete(Long aLong) {
        //LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //queryWrapper.eq(Setmeal::getId,id);
        //Setmeal setmeal = setmealMapper.selectById(id);
        //if (setmeal.getStatus() != 0){
        //    throw new CustomException("当前套餐为售卖状态，不能删除！");
        //}
        //setmealMapper.deleteById(id);
        //LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        //setmealDishMapper.delete(setmealDishLambdaQueryWrapper);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getId,aLong);
        Dish dish = dishMapper.selectById(aLong);
        if (dish.getStatus() != 0){
            throw new CustomException("当前菜品为售卖状态，不能删除！");
        }
        LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(DishFlavor::getDishId,aLong);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(queryWrapper1);
        if (dishFlavors != null){
            dishFlavorMapper.delete(queryWrapper1);
        }
        LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(SetmealDish::getDishId,aLong);
        Long selectCount = setmealDishMapper.selectCount(queryWrapper2);
        if (selectCount > 0 ){
            throw new CustomException("当前菜品存在关联套餐，不能删除！请先在套餐中删除菜品！");
        }
        redisUtils.removePicFromRedis(dish.getImage());
        dishMapper.delete(queryWrapper);


    }


    @Override
    public DishDto getByWithFlavor(Long id) {
        Dish dish = dishMapper.selectById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(queryWrapper);
        dishDto.setFlavors(dishFlavors);
        //dishFlavorMapper.selectById(id);
        return dishDto;
    }
}
