package com.gig.jpastudy.api;

import com.gig.jpastudy.dto.OrderDto;
import com.gig.jpastudy.dto.OrderLightDto;
import com.gig.jpastudy.dto.OrderSearchDto;
import com.gig.jpastudy.model.Order;
import com.gig.jpastudy.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/api/v3/simple-orders")
    public List<OrderLightDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<OrderLightDto> result = orders.stream()
                .map(o -> new OrderLightDto(o))
                .collect(Collectors.toList());

        return result;
    }


}
