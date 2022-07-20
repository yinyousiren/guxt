package com.guxt.take.controller;


import com.guxt.take.common.R;
import com.guxt.take.entity.OrderDetail;
import com.guxt.take.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/{id}")
    public R<List<OrderDetail>> getById(@PathVariable Long id){

        return  orderDetailService.getByOrderId(id);

    }

}
