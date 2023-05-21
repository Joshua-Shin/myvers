package com.example.myvers.friend;

import com.example.myvers.domain.Friend;
import com.example.myvers.domain.Gender;
import com.example.myvers.domain.Mbti;
import com.example.myvers.domain.Member;
import com.example.myvers.repository.FriendRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FriendRepositoryTest {
    @Autowired
    FriendRepository friendRepository;

    @Test
    @DisplayName("친구 생성")
    public void save() throws Exception {
        //given
        Friend friend = Friend.builder()
                .name("이지은")
                .age(19)
                .gender(Gender.FEMALE)
                .mbti(Mbti.ENTP)
                .member(Member.builder().build())
                .build();

        //when
        Long saveId = friendRepository.save(friend); //
        Friend findFriend = friendRepository.findById(saveId);
        //then
        Assertions.assertThat(friend).isEqualTo(findFriend);
    }
    @Test
    @DisplayName("친구 삭제")
    public void delete() throws Exception {
        //given
        Friend friend = Friend.builder()
                .name("이지은")
                .age(19)
                .gender(Gender.FEMALE)
                .mbti(Mbti.ENTP)
                .member(Member.builder().build())
                .build();

        //when
        Long saveId = friendRepository.save(friend);
        friendRepository.delete(saveId);
        //then
        Assertions.assertThat(friendRepository.findById(saveId)).isEqualTo(null);
    }
}