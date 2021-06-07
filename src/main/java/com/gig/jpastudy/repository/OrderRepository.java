package com.gig.jpastudy.repository;

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

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class
        ).getResultList();
    }
    // 엔티티를 패치 조인을 사용해서 쿼리 1번에 조인
    // 패치 조인으로 order -> member, order -> delivery 는 이미 조회된 상태이므로 지연로딩 X
}
