package com.gig.jpastudy.repository;

import com.gig.jpastudy.dto.OrderFlatDto;
import com.gig.jpastudy.dto.OrderItemDto;
import com.gig.jpastudy.dto.OrderLightDto;
import com.gig.jpastudy.dto.OrderSearchDto;
import com.gig.jpastudy.model.Member;
import com.gig.jpastudy.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public List<Order> findAllByStringAndJPQL(OrderSearchDto orderSearchDto) {

        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;

        // 주문상태 검색
        if (orderSearchDto.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.orderStatus = :orderStatus";
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearchDto.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);

        if (orderSearchDto.getOrderStatus() != null) {
            query = query.setParameter("orderStatus", orderSearchDto.getOrderStatus());
        }

        if (StringUtils.hasText(orderSearchDto.getMemberName())) {
            query = query.setParameter("name", orderSearchDto.getMemberName());
        }

        return query.getResultList();
    }

    public List<Order> findAllByCriteria(OrderSearchDto orderSearchDto) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearchDto.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("orderStatus"), orderSearchDto.getOrderStatus());
            criteria.add(status);
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearchDto.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearchDto.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);

        return query.getResultList();
    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class
        )
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
    // 엔티티를 패치 조인을 사용해서 쿼리 1번에 조인
    // 패치 조인으로 order -> member, order -> delivery 는 이미 조회된 상태이므로 지연로딩 X


    public List<OrderLightDto> findOrderQueryDtos() {

        List<OrderLightDto> result = findOrders();

        result.forEach(o -> {
            List<OrderItemDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;
    }

    /**
     * 1 : N 관계(컬렉션)를 제외한 나머지를 한번에 조회
     * @return
     */
    private List<OrderLightDto> findOrders() {
        return em.createQuery(
                "select new com.gig.jpastudy.dto.OrderLightDto(o.orderId, m.name, o.orderDate, o.orderStatus, d.address) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d", OrderLightDto.class)
                .getResultList();
    }


    /**
     * 1:N 관계인 orderItems 조회
     */
    private List<OrderItemDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new com.gig.jpastudy.dto.OrderItemDto(oi.order.orderId, i.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi " +
                        "join oi.item i " +
                        "where oi.order.orderId = :orderId", OrderItemDto.class
                ).setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderLightDto> findAllByDto_optimization() {

        List<OrderLightDto> result = findOrders();

        Map<Long, List<OrderItemDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private List<Long> toOrderIds(List<OrderLightDto> result) {
        return result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemDto> orderItems = em.createQuery(
                "select new com.gig.jpastudy.dto.OrderItemDto(oi.order.orderId, i.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi " +
                        "join oi.item i " +
                        "where oi.order.orderId in :orderIds", OrderItemDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        return orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemDto::getOrderId));
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new com.gig.jpastudy.dto.OrderFlatDto(o.orderId, m.name, o.orderDate, o.orderStatus, " +
                        "d.address, i.name, oi.orderPrice, oi.count) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d " +
                        "join o.orderItems oi " +
                        "join oi.item i ", OrderFlatDto.class)
                .getResultList();
    }
}
