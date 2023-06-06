package com.example.myvers.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String loginId;

    private String password;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private LocalDateTime createdDate;

    private Long talkCount; // 친구 삭제 시에도 전체 메시지 수가 변동 없음.

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Friend> friends = new ArrayList<>();

    @Builder
    public Member(String loginId, String password, String name, String email, Grade grade,
                  LocalDateTime createdDate) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.grade = grade;
        this.createdDate = createdDate;
        this.talkCount = 1L;
    }

    public void change(String password, String name, String email) {
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void addTalkCount() {
        talkCount++;
    }
}
