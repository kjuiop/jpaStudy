package com.gig.jpastudy.dto;

import com.gig.jpastudy.model.Order;
import com.gig.jpastudy.model.embedded.Address;
import com.gig.jpastudy.types.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Jake
 * @date : 2021-06-07
 */
@Data
public class OrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
        orderId = order.getOrderId();
        name = order.getMember().getName(); orderDate = order.getOrderDate();
        orderStatus = order.getOrderStatus();
        address = order.getDelivery().getAddress();
        orderItems = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemDto(orderItem)).collect(Collectors.toList());
    }
}
