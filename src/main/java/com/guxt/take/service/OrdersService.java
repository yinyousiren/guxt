package com.guxt.take.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guxt.take.common.R;
import com.guxt.take.entity.Orders;


import java.time.LocalDateTime;

public interface OrdersService extends IService<Orders> {
    /**
     * 订单分页查询、条件查询
     * @param pageInfo
     * @param number
     * @param startTime
     * @param endTime
     * @return
     */
    R<Page> pageQuery(Page pageInfo, String number, LocalDateTime startTime, LocalDateTime endTime);
}
