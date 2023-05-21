package com.example.myvers.controller;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendDeleteForm {
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @Builder
    public FriendDeleteForm(String password) {
        this.password = password;
    }
}
