package com.guxt.take.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.guxt.take.common.R;
import com.guxt.take.dto.OrdersDto;
import com.guxt.take.entity.Orders;
import com.guxt.take.mapper.OrdersMapper;
import com.guxt.take.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Override
    public R<Page> pageQuery(Page pageInfo, String number, LocalDateTime startTime, LocalDateTime endTime) {
        Page<OrdersDto> ordersDtoPage = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(number),Orders::getNumber,number);

        //Page<SetmealDto> setmealDtoPage = new Page<>();
        //LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //queryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        //queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //
        //setmealMapper.selectPage(pageInfo, queryWrapper);
        //
        //BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        //
        //List<Setmeal> records = pageInfo.getRecords();
        //List<SetmealDto> setmealDtoList = new ArrayList<>();
        //
        //for (Setmeal record : records) {
        //    SetmealDto setmealDto = new SetmealDto();
        //    BeanUtils.copyProperties(record, setmealDto);
        //    Long categoryId = record.getCategoryId();
        //    Category category = categoryMapper.selectById(categoryId);
        //    if (category != null) {
        //        String categoryName = category.getName();
        //        setmealDto.setCategoryName(categoryName);
        //    }
        //    setmealDtoList.add(setmealDto);
        //
        //}
        //setmealDtoPage.setRecords(setmealDtoList);
        //return R.success(setmealDtoPage);
        return null;
    }
}
