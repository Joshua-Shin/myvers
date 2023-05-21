package com.example.myvers.controller;

import com.example.myvers.domain.Grade;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseMemberForm {

    private Long id;

    private String loginId;

    private String password;

    private String name;

    private String email;

    private Grade grade;

    private LocalDateTime createdDate;

    private final List<Pair<Long, String>> friends = new ArrayList<>(); // friendId, friendName

    @Builder
    public ResponseMemberForm(Long id, String loginId, String password, String name, String email, Grade grade,
                              LocalDateTime createdDate) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.grade = grade;
        this.createdDate = createdDate;
    }
}
