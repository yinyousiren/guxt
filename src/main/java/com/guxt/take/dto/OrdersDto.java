package com.guxt.take.dto;


import com.guxt.take.entity.OrderDetail;
import com.guxt.take.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
