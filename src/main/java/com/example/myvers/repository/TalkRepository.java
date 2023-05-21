package com.example.myvers.repository;

import com.example.myvers.domain.Talk;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TalkRepository {
    private final EntityManager em;

    // 대화 저장
    public Long save(Talk talk) {
        em.persist(talk);
        return talk.getId();
    }


}
