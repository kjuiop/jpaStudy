package com.gig.jpastudy.api;

import com.gig.jpastudy.dto.*;
import com.gig.jpastudy.model.Order;
import com.gig.jpastudy.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByStringAndJPQL(new OrderSearchDto());
        for (Order order : all) {
            // lazy 강제 초기화
            order.getMember().getName();
            // lazy 강제 초기화
            order.getDelivery().getAddress();
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderV2() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderLightDto> ordersV3(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderLightDto> result = orders.stream()
                .map(o -> new OrderLightDto(o))
                .collect(Collectors.toList());

        return result;
    }


    @GetMapping("/api/v4/orders")
    public List<OrderLightDto> orderV4() {
        return orderRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderLightDto> orderV5() {
        return orderRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderLightDto> ordersV6() {
        List<OrderFlatDto> flats = orderRepository.findAllByDto_flat();

        return null;
//        return flats.stream()
//                .collect(groupingBy(o -> new OrderLightDto(o.getOrderId(),
//                                o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
//                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
//                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
//                )).entrySet().stream()
//                .map(e -> new OrderLightDto(e.getKey().getOrderId(),
//                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
//                        e.getKey().getAddress(), e.getValue()))
//                .collect(toList());
    }


}
