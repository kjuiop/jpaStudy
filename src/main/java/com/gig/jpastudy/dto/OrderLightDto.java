package com.gig.jpastudy.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gig.jpastudy.model.Order;
import com.gig.jpastudy.model.embedded.Address;
import com.gig.jpastudy.types.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Jake
 * @date : 2021-06-07
 */
@Data
@EqualsAndHashCode(of = "orderId")
public class OrderLightDto {

    @JsonIgnore
    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;

    private List<OrderItemDto> orderItems = new ArrayList<>();


    public OrderLightDto(Order order) {
        orderId = order.getOrderId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getOrderStatus();
        address = order.getDelivery().getAddress();
    }

    public OrderLightDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
