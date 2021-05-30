package com.gig.jpastudy.controller;

import com.gig.jpastudy.dto.OrderSearchDto;
import com.gig.jpastudy.model.Member;
import com.gig.jpastudy.model.Order;
import com.gig.jpastudy.model.item.Item;
import com.gig.jpastudy.service.ItemService;
import com.gig.jpastudy.service.MemberService;
import com.gig.jpastudy.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping(value = "/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearchDto orderSearchDto,
                            Model model) {

        List<Order> orders = orderService.findOrders(orderSearchDto);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }


    @GetMapping(value = "/order")
    public String createForm(Model model) {

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping(value = "/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {

        orderService.order(memberId, itemId, count);

        return "redirect:/orders";
    }

    @PostMapping(value = "/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

}
