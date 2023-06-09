package com.example.myvers.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.myvers.domain.Friend;
import com.example.myvers.domain.Member;
import com.example.myvers.repository.FriendRepository;
import com.example.myvers.repository.MemberRepository;
import com.example.myvers.service.FriendService;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;
    @Autowired
    FriendRepository friendRepository;

    @Test
    @DisplayName("회원 저장")
    public void save() throws Exception {
        //given
        Member member = Member.builder()
                .name("신중혁")
                .build();
        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.findById(saveId);
        //then
        assertThat(member).isEqualTo(findMember);

    }

    @Test
    @DisplayName("전체 회원 조회")
    public void findAll() throws Exception {
        //given
        Member member1 = Member.builder()
                .name("신중혁")
                .build();
        Member member2 = Member.builder()
                .name("변서율")
                .build();
        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        List<Member> members = memberRepository.findAll();
        //then
        assertThat(members).contains(member1, member2);
    }
    @Test
    @DisplayName("로그인아이디로 조회")
    public void findByLoginId() throws Exception {
        //given
        Member member = Member.builder()
                .name("신중혁")
                .loginId("abc1234")
                .build();
        //when
        memberRepository.save(member);
        Optional<Member> memberOptional1 = memberRepository.findByLoginId("abc1234");
        Optional<Member> memberOptional2 = memberRepository.findByLoginId("cde1111");
        //then
        assertThat(memberOptional1.isPresent()).isTrue();
        assertThat(memberOptional2.isPresent()).isFalse();
    }

    @Test
    @DisplayName("회원 불러와서 getFriends 할때,  default_batch_fetch_size 설정 여부에 따른 쿼리 개수 확인")
    public void findOne() throws Exception {
        //given
        Member member = Member.builder()
                .name("userA")
                .build();

        Friend friendA = Friend.builder()
                .name("friendA")
                .member(member)
                .build();
        Friend friendB = Friend.builder()
                .name("friendB")
                .member(member)
                .build();
        Friend friendC = Friend.builder()
                .name("friendC")
                .member(member)
                .build();
        Friend friendD = Friend.builder()
                .name("friendD")
                .member(member)
                .build();
        Friend friendE = Friend.builder()
                .name("friendE")
                .member(member)
                .build();

        //when
        memberRepository.save(member);
        friendRepository.save(friendA);
        friendRepository.save(friendB);
        friendRepository.save(friendC);
        friendRepository.save(friendD);
        friendRepository.save(friendE);

        em.flush();
        em.clear();

        //then
        Member findMember = memberRepository.findById(member.getId());
        List<Friend> friends = findMember.getFriends();
        for (Friend friend : friends) {
            log.info("getClass: " + friend.getClass());
            log.info("getClass: " + friend.getName());
        }
    }

}