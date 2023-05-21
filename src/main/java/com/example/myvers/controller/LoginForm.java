package com.example.myvers.controller;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginForm {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Builder
    public LoginForm(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
