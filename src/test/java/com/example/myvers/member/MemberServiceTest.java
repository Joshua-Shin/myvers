package com.example.myvers.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.myvers.domain.Member;
import com.example.myvers.repository.MemberRepository;
import com.example.myvers.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입")
    public void join() throws Exception {
        //given
        Member member = Member.builder()
                .name("신중혁")
                .build();
        //when
        Long joinId = memberService.join(member);
        Member findMember = memberRepository.findById(joinId);

        //then
        assertThat(findMember).isEqualTo(member);
    }

}