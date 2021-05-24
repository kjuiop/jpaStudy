package com.gig.jpastudy.service;

import com.gig.jpastudy.model.Delivery;
import com.gig.jpastudy.model.Member;
import com.gig.jpastudy.model.Order;
import com.gig.jpastudy.model.OrderItem;
import com.gig.jpastudy.model.item.Item;
import com.gig.jpastudy.repository.ItemRepository;
import com.gig.jpastudy.repository.MemberRepository;
import com.gig.jpastudy.repository.OrderRepository;
import com.gig.jpastudy.types.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setDeliveryStatus(DeliveryStatus.READY);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        return order.getOrderId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }


}
