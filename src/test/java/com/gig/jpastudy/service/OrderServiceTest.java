package com.gig.jpastudy.service;

import com.gig.jpastudy.exception.NotEnoughStockException;
import com.gig.jpastudy.model.Member;
import com.gig.jpastudy.model.Order;
import com.gig.jpastudy.model.embedded.Address;
import com.gig.jpastudy.model.item.Book;
import com.gig.jpastudy.model.item.Item;
import com.gig.jpastudy.repository.OrderRepository;
import com.gig.jpastudy.types.OrderStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
/**
 * @author : Jake
 * @date : 2021-05-28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {

        // Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        // When
        Long orderId = orderService.order(member.getMemberId(), item.getItemId(), orderCount);

        // Then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, findOrder.getOrderStatus());

        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, findOrder.getOrderItems().size());

        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * 2, findOrder.getTotalPrice());

        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, item.getStockQuantity());

    }

    @Test
    public void 주문취소() {

        // Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getMemberId(), item.getItemId(), orderCount);

        // When
        orderService.cancelOrder(orderId);


        // Then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getOrderStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {

        // Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);

        int orderCount = 11;

        // When
        orderService.order(member.getMemberId(), item.getItemId(), orderCount);

        // Then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }


    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "경기", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }

}
