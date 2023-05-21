package com.example.myvers.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Talk {
    @Id
    @GeneratedValue
    @Column(name="talk_id")
    private Long id;

    @Lob
    private String message;

    @Enumerated(EnumType.STRING)
    private Speaker speaker;

    private LocalDateTime createdDateTime; // 2023-05-09T03:54:00.000
    private String time; // 오전 3:54

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Friend friend;

    // ==== 연관관계 편의 메소드 ==== //
    public void setFriend(Friend friend) {
        this.friend = friend;
        friend.getTalks().add(this);
    }
    @Builder
    public Talk(String message, Speaker speaker, LocalDateTime createdDateTime, String time, Friend friend) {
        this.message = message;
        this.speaker = speaker;
        this.createdDateTime = createdDateTime;
        this.time = time;
        this.setFriend(friend);
    }
}
