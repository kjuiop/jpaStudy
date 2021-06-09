package com.gig.jpastudy.dto;

import com.gig.jpastudy.model.OrderItem;
import lombok.Data;

/**
 * @author : Jake
 * @date : 2021-06-07
 */
@Data
public class OrderItemDto {

    private Long orderId;
    private String itemName;//상품 명
    private int orderPrice; //주문 가격
    private int count; //주문 수량

    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getName();
        orderPrice = orderItem.getOrderPrice();
        count = orderItem.getCount();
    }

    public OrderItemDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
