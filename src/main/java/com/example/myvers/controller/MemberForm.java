package com.example.myvers.controller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberForm {

    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min=6, max=16, message = "길이가 맞지 않습니다")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min=8, max=16, message = "길이가 맞지 않습니다")
    private String password;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min=8, max=16, message = "길이가 맞지 않습니다")
    private String confirmPassword;

    @NotBlank(message = "이름을 입력해주세요")
    @Size(min=2, max=6, message = "길이가 맞지 않습니다")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @Builder
    public MemberForm(String loginId, String password, String confirmPassword, String name, String email) {
        this.loginId = loginId;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.name = name;
        this.email = email;
    }
}
