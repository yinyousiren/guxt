package com.guxt.take.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guxt.take.common.R;
import com.guxt.take.dto.OrdersDto;
import com.guxt.take.entity.OrderDetail;
import com.guxt.take.entity.Orders;
import com.guxt.take.service.OrderDetailService;
import com.guxt.take.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.xml.XMLConstants;
import javax.xml.ws.soap.Addressing;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("下单成功！");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number,String beginTime,String endTime){

        Page pageInfo = new Page<>(page,pageSize);
        return orderService.pageQuery(pageInfo,number,beginTime,endTime);
    }
    @PutMapping
    public R<String> status(@RequestBody OrdersDto ordersDto){
        orderService.updateStatusById(ordersDto);
        return R.success("修改订单状态成功！");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page,int pageSize){
        Page pageInfo = new Page<>(page,pageSize);
        return orderService.userPage(pageInfo);
    }


    @DeleteMapping
    @Transactional(rollbackFor = Exception.class)
    public R<String> deleteOrder(Long id){
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,id);
        orderDetailService.remove(queryWrapper);
        orderService.removeById(id);
        return R.success("删除成功！");
    }


    @PostMapping("/again")
    public R<String> addOrderAgain(@RequestBody Orders orders){
        if (orders.getId() != null){
            return R.success("成功！");
        }
        return R.error("失败!");
    }

    @GetMapping("/getToDayOrder")
    public R<Long> getToDayOrder(){
       return orderService.countToDayOrder();
    }

    @GetMapping("/getYesDayOrder")
    public R<Long> getYesDayOrder(){
        return orderService.countYesDayOrder();
    }

    @GetMapping("/getOneWeekLiuShui")
    public R<Map> getOneWeekLiuShui(){
        return orderService.OneWeekLiuShui();
    }

    @GetMapping("/getOneWeekOrder")
    public R<Map> getOneWeekOrder(){
        return orderService.OneWeekOrder();
    }

    @GetMapping("/getHotSeal")
    public R<Map> getHotSeal(){
        return orderService.hotSeal();
    }

}
