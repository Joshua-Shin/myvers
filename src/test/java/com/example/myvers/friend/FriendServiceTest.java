package com.example.myvers.friend;

import com.example.myvers.domain.Friend;
import com.example.myvers.domain.Gender;
import com.example.myvers.domain.Grade;
import com.example.myvers.domain.Mbti;
import com.example.myvers.domain.Member;
import com.example.myvers.repository.FriendRepository;
import com.example.myvers.service.FriendService;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FriendServiceTest {

    @Autowired
    FriendService friendService;
    @Autowired
    FriendRepository friendRepository;

    @Test
    @DisplayName("친구 생성")
    public void generateFriend() throws Exception {
        //given
        Member member = Member.builder()
                .grade(Grade.NORMAL)
                .build();
        Friend friend = Friend.builder()
                .name("이지은")
                .age(19)
                .createdDate(LocalDateTime.now())
                .mbti(Mbti.INFJ)
                .gender(Gender.FEMALE)
                .member(member)
                .build();
        Long friendId = friendService.generateFriend(friend);

        //when
        Friend findFriend = friendRepository.findById(friendId);

        //then
        Assertions.assertThat(findFriend).isEqualTo(friend);
    }

}