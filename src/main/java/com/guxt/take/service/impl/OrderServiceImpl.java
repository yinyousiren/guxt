package com.guxt.take.service.impl;

import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.AtomicDouble;
import com.guxt.take.common.R;
import com.guxt.take.dto.OrdersDto;
import com.guxt.take.entity.*;
import com.guxt.take.excption.CustomException;
import com.guxt.take.mapper.*;
import com.guxt.take.service.OrderDetailService;
import com.guxt.take.service.OrderService;
import com.guxt.take.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderDetailService orderDetailService;


    @Override
    public void submit(Orders orders) {
        //获得当前用户id
        Long userId = UserThreadLocal.get().getId();

        //查询当前用户购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(queryWrapper);
        if (shoppingCarts == null || shoppingCarts.size() == 0){
            throw new CustomException("购物车为空，不能下单！");
        }
        //查询用户数据
        User user = userMapper.selectById(userId);
        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookMapper.selectById(addressBookId);
        if (addressBook == null ){
            throw new CustomException("地址信息有错误，不能下单！");
        }

        //向订单表插入数据，一条数据
        long orderId = IdWorker.getId();
        AtomicDouble amount = new AtomicDouble(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).doubleValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        orderMapper.insert(orders);

        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);


        //清空购物车数据
        shoppingCartMapper.delete(queryWrapper);
    }

    @Override
    public R<Page> pageQuery(Page pageInfo, String number, String beginTime, String endTime) {
        Page<OrdersDto> ordersDtoPage = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(number),Orders::getNumber,number);
        queryWrapper.between(StringUtils.isNotEmpty(beginTime) || StringUtils.isNotEmpty(endTime),Orders::getOrderTime,beginTime,endTime);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderMapper.selectPage(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtoList = new ArrayList<>();
        for (Orders record : records) {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(record,ordersDto);
            Long userId = record.getUserId();

            User user = userMapper.selectById(userId);

            if (user != null){
                String name = user.getNickName();
                ordersDto.setUserName(name);
            }
            ordersDtoList.add(ordersDto);
        }
        ordersDtoPage.setRecords(ordersDtoList);
        return R.success(ordersDtoPage);
    }
    @Override
        public R<Page> userPage(Page pageInfo) {

        try {
            Page<OrdersDto> dtoPage = new Page<>();
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByDesc(Orders::getOrderTime);
            queryWrapper.eq(Orders::getUserId,UserThreadLocal.get().getId());
            orderMapper.selectPage(pageInfo,queryWrapper);
            BeanUtils.copyProperties(pageInfo,dtoPage,"records");
            List<Orders> records = pageInfo.getRecords();
            List<OrdersDto> ordersDtoList = new ArrayList<>();
            for (Orders record : records) {
                OrdersDto ordersDto = new OrdersDto();
                BeanUtils.copyProperties(record,ordersDto);
                Long userId = record.getUserId();
                LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(OrderDetail::getOrderId,record.getId());
                List<OrderDetail> orderDetails = orderDetailMapper.selectList(queryWrapper1);
                ordersDto.setOrderDetails(orderDetails);

                User user = userMapper.selectById(userId);

                if (user != null){
                    String name = user.getNickName();
                    ordersDto.setUserName(name);
                }
                ordersDtoList.add(ordersDto);
            }
            dtoPage.setRecords(ordersDtoList);
            return R.success(dtoPage);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("订单数据加载失败");
        }

    }

    @Override
    public R<Map> hotSeal() {
        Map<String,Object> data = new HashMap<>();

        QueryWrapper<OrderDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.select("COUNT(dish_id) as dishCount,name ").groupBy("name").having("COUNT(dish_id)").orderByDesc("dishCount").last("LIMIT 2");
        List<Map<String,Object>> orderDetails = orderDetailMapper.selectMaps(detailQueryWrapper);
        //count
        List<String> dishCount = new ArrayList<>();
        for (Map<String, Object> orderDetail : orderDetails) {
            String count = String.valueOf(orderDetail.get("dishCount"));
            dishCount.add(count);
        }
        data.put("dishCount",orderDetails);




        QueryWrapper<OrderDetail> detailQueryWrapper1 = new QueryWrapper<>();
        detailQueryWrapper1.select("COUNT(setmeal_id) as setmealCount,name ").groupBy("name").having("COUNT(setmeal_id)").orderByDesc("setmealCount").last("LIMIT 2");
        List<Map<String,Object>> setmealDetails = orderDetailMapper.selectMaps(detailQueryWrapper1);
        //count
        List<String> setmealCount = new ArrayList<>();
        for (Map<String, Object> setmealDetail : setmealDetails) {
            String count = String.valueOf(setmealDetail.get("setmealCount"));
            setmealCount.add(count);
        }
        data.put("setmealCount",setmealDetails);


        return R.success(data);
    }

    @Override
    public R<Map> OneWeekOrder() {

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusDays(-7);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).plusDays(-7);
        LocalDate startDay = LocalDate.now().plusDays(-7);

        Map<String,Object> map = new HashMap<>();
        List<Long> orderCount = new ArrayList<>();
        List<LocalDate> days = new ArrayList<>();
        for (int i = 0;i<7;i++){
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.between(Orders::getOrderTime,start,end);
            Long aLong = orderMapper.selectCount(queryWrapper);
            orderCount.add(aLong);
            start = start.plusDays(1);
            end = end.plusDays(1);
            days.add(startDay);
            startDay = startDay.plusDays(1);
        }
        map.put("orderCount",orderCount);
        map.put("days",days);
        return R.success(map);
    }

    @Override
    public R<Map> OneWeekLiuShui() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusDays(-7);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).plusDays(-7);
        LocalDate startDay = LocalDate.now().plusDays(-7);

        Map<String,Object> map = new HashMap<>();
        List<Double> amount = new ArrayList<>();
        List<LocalDate> days = new ArrayList<>();
        for (int i = 0;i<7;i++){
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.between(Orders::getOrderTime,start,end);
            List<Orders> orders = orderMapper.selectList(queryWrapper);
            double orderAmount = 0;
            for (Orders order : orders) {
                orderAmount += order.getAmount().doubleValue();
            }
            amount.add(orderAmount);
            start = start.plusDays(1);
            end = end.plusDays(1);
            days.add(startDay);
            startDay = startDay.plusDays(1);

        }
        map.put("amount",amount);
        map.put("days",days);
        return R.success(map);
    }

    @Override
    public R<Long> countYesDayOrder() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusDays(-1);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).plusDays(-1);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(Orders::getOrderTime,start,end);
        Long aLong = orderMapper.selectCount(queryWrapper);


        return R.success(aLong);
    }

    @Override
    public R<Long> countToDayOrder() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(Orders::getOrderTime,start,end);
        Long aLong = orderMapper.selectCount(queryWrapper);
        return R.success(aLong);
    }

    @Override
    public void updateStatusById(OrdersDto ordersDto) {
        Long id = ordersDto.getId();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getId,id);
        Orders orders = orderMapper.selectOne(queryWrapper);
        orders.setStatus(ordersDto.getStatus());
        orderMapper.updateById(orders);
    }


}
