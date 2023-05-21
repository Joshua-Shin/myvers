package com.example.myvers.service;

import com.example.myvers.domain.Member;
import com.example.myvers.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    // 회원 전체 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
    // 회원 단건 조회
    public Member findOne(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional
    public void update(Long memberId, String newPassword, String name, String email) {
        Member member = memberRepository.findById(memberId);
        member.change(newPassword, name, email);
    }

//     로그인 아이디로 조회
    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    //     이메일로 조회
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

}
