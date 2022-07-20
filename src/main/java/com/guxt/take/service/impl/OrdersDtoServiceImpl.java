package com.guxt.take.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxt.take.dto.OrdersDto;
import com.guxt.take.mapper.OrdersDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrdersDtoServiceImpl extends ServiceImpl<OrdersDtoMapper, OrdersDto> {
}
