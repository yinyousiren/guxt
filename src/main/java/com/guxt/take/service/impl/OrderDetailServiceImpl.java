package com.guxt.take.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxt.take.common.R;
import com.guxt.take.entity.OrderDetail;
import com.guxt.take.mapper.OrderDetailMapper;
import com.guxt.take.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public R<List<OrderDetail>> getByOrderId(Long id) {
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,id);
        List<OrderDetail> orderDetails = orderDetailMapper.selectList(queryWrapper);
        AtomicInteger amount = new AtomicInteger(0);
        return R.success(orderDetails);
    }
}
