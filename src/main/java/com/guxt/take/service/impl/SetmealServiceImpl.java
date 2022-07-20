package com.guxt.take.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxt.take.common.R;
import com.guxt.take.dto.SetmealDto;
import com.guxt.take.entity.Category;
import com.guxt.take.entity.Setmeal;
import com.guxt.take.entity.SetmealDish;
import com.guxt.take.excption.CustomException;
import com.guxt.take.mapper.CategoryMapper;
import com.guxt.take.mapper.SetmealDishMapper;
import com.guxt.take.mapper.SetmealMapper;
import com.guxt.take.service.SetmealService;
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
import java.util.Set;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private RedisUtils redisUtils;
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value ="SetmealCache",allEntries = true)
    public void delete(Long id) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getId,id);
        Setmeal setmeal = setmealMapper.selectById(id);
        if (setmeal.getStatus() != 0){
            throw new CustomException("当前套餐为售卖状态，不能删除！");
        }
        setmealMapper.deleteById(id);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        redisUtils.removePicFromRedis(setmeal.getImage());
        setmealDishMapper.delete(setmealDishLambdaQueryWrapper);

    }

    @Override
    @Cacheable(value = "SetmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
    public R<List<Setmeal>> findSetmealByCategoryId(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmeals = setmealMapper.selectList(queryWrapper);
        return R.success(setmeals);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value ="SetmealCache",allEntries = true)
    public void saveWithDishes(SetmealDto setmealDto) {
        setmealMapper.insert(setmealDto);
        Long setmealDtoId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDtoId);
            setmealDishMapper.insert(setmealDish);
        }
        String image = setmealDto.getImage();
        redisUtils.save2Db(image);

    }

    @Override
    public R<Page> pageQuery(Page pageInfo, String name) {
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealMapper.selectPage(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList = new ArrayList<>();

        for (Setmeal record : records) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(record, setmealDto);
            Long categoryId = record.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            setmealDtoList.add(setmealDto);

        }
        setmealDtoPage.setRecords(setmealDtoList);
        return R.success(setmealDtoPage);
    }

    @Override
    public SetmealDto getByWithDish(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(dishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);
        //setmealMapper.selectById(id);

        return setmealDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value ="SetmealCache",allEntries = true)
    public void updateWithDishes(SetmealDto setmealDto) {
        setmealMapper.updateById(setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishMapper.delete(queryWrapper);
        Long id = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
            setmealDishMapper.insert(setmealDish);
        }
        String image = setmealDto.getImage();
        redisUtils.save2Db(image);
    }

    @Override
    @CacheEvict(value ="SetmealCache",allEntries = true)
    public void updateStatusById(Long id) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getId,id);
        Setmeal setmeal = setmealMapper.selectById(id);
        if (setmeal.getStatus() == 0 ){
            setmeal.setStatus(1);
        }else{
            setmeal.setStatus(0);
        }
        setmealMapper.updateById(setmeal);
    }
}