package com.example.myvers.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {
    @Id
    @GeneratedValue
    @Column(name="friend_id")
    private Long id;

    private String name;

    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    private LocalDateTime createdDate;

    private String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Talk> talks = new ArrayList<>();

    // ==== 연관관계 편의 메소드 ==== //
    public void setMember(Member member) {
        this.member = member;
        member.getFriends().add(this);
    }
    @Builder
    public Friend(String name, int age, Gender gender, Mbti mbti, LocalDateTime createdDate, String imageName,
                  Member member) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.mbti = mbti;
        this.createdDate = createdDate;
        this.imageName = imageName;
        this.setMember(member);
    }
}
