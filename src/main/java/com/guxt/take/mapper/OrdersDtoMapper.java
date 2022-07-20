package com.guxt.take.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guxt.take.dto.OrdersDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersDtoMapper extends BaseMapper<OrdersDto> {
}
