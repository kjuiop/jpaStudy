package com.gig.jpastudy.service;

import com.gig.jpastudy.model.Member;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class MemberService {

    @PersistenceContext
    EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getMemberId();
    }

    public Member findMember(Long memberId) {
        return em.find(Member.class, memberId);
    }


}
