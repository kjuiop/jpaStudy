package com.gig.jpastudy.service;

import com.gig.jpastudy.model.Member;
import com.gig.jpastudy.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {



    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void 회원가입() {
        // Given
        Member member = new Member();
        member.setUsername("kim");

        // When
        Long savedId = memberService.join(member);

        // Then
        assertEquals(member, memberRepository.findOne(savedId));
//        Member findMember = memberService.findMember(savedId);
//        Assertions.assertThat(findMember.getMemberId()).isEqualTo(savedId);
//        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//        Assertions.assertThat(findMember).isEqualTo(member);

    }

    /**
     * IllegalSTateException 이 발생하면 성공
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("jake");
        Member member2 = new Member();
        member2.setName("jake");
        //When
        memberService.join(member1);
        memberService.join(member2);

//        try {
//            memberService.join(member2);
//        } catch (IllegalStateException e) {
//            return;
//        }
         //예외가 발생해야 한다.
        //Then
        fail("예외가 발생해야 한다.");
    }

}

/**
 * @RunWith(SpringRunner.class)
 * JUnit 을 사용할 때 Spring 이랑 엮어서 쓸래.
 * @SpringBootTest
 * 스프링 부트랑 같이 쓸래
 */