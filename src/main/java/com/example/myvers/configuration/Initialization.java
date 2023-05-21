package com.example.myvers.configuration;

import com.example.myvers.domain.Friend;
import com.example.myvers.domain.Gender;
import com.example.myvers.domain.Grade;
import com.example.myvers.domain.Mbti;
import com.example.myvers.domain.Member;
import com.example.myvers.service.FriendService;
import com.example.myvers.service.MemberService;
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class Initialization {
    private final MemberService memberService;
    private final FriendService friendService;

    // admin 계정 만들어두기
    @PostConstruct
    public void init() {
        // master 계정
        Member member = Member.builder()
                .grade(Grade.ADMIN)
                .email("sjh910805@gmail.com")
                .loginId("sjh910805")
                .password("shin4297")
                .name("중혁")
                .createdDate(LocalDateTime.now())
                .build();
        memberService.join(member);

        String [] name = {"채원", "이지은", "데이나", "소옥", "허윤진", "모현민", "김유정", "장원영", "신예은", "제니"};
        for(int i = 1; i <= 10; i++) {
            Friend friend = Friend.builder()
                    .imageName("/image/f" + i + ".jpeg")
                    .member(member)
                    .age(20)
                    .mbti(Mbti.INFJ)
                    .gender(Gender.FEMALE)
                    .name(name[i - 1])
                    .createdDate(LocalDateTime.now())
                    .build();
            friendService.generateFriend(friend);
        }

        // normal 계정
        Member member2 = Member.builder()
                .grade(Grade.NORMAL)
                .email("abcd1234@gmail.com")
                .loginId("sjh910806")
                .password("shin4297")
                .name("중혁")
                .createdDate(LocalDateTime.now())
                .build();
        memberService.join(member2);

        // 회원 98개 추가.
//        for(int i = 0; i < 98; i++) {
//            Member member3 = Member.builder()
//                    .grade(Grade.NORMAL)
//                    .email("abcd1234@gmail.com")
//                    .loginId("sjh910806" + i)
//                    .password("shin4297")
//                    .name("중혁2")
//                    .createdDate(LocalDateTime.now())
//                    .build();
//            memberService.join(member3);
//        }
    }

}
