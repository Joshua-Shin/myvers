package com.example.myvers.repository;

import com.example.myvers.domain.Friend;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendRepository {
    private final EntityManager em;

    // 친구생성
    public Long save(Friend friend) {
        em.persist(friend);
        return friend.getId();
    }

    // 친구조회
    public Friend findById(Long id) {
        return em.find(Friend.class, id);
    }

    // 친구전체 조회
    public List<Friend> findAll() {
        return em.createQuery("select f from friend f", Friend.class)
                .getResultList();
    }

    // 친구삭제
    public void delete(Long id) {
        em.remove(findById(id));
    }


}
