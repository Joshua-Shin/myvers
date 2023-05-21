package com.example.myvers.repository;

import com.example.myvers.domain.Member;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }
    // 회원 단건 조회
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    // 회원 loginId로 조회
    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findAny();
    }
    // 회원 email로 조회
    public Optional<Member> findByEmail(String email) {
        return findAll().stream()
                .filter(m -> m.getEmail().equals(email))
                .findAny();
    }

    // 회원 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

}
