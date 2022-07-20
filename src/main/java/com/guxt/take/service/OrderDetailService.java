package com.guxt.take.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guxt.take.common.R;
import com.guxt.take.entity.OrderDetail;


import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {
    /**
     * 查询订单详细信息
     * @param id
     * @return
     */
    R<List<OrderDetail>> getByOrderId(Long id);
}
