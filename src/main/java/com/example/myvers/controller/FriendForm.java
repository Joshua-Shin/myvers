package com.example.myvers.controller;

import com.example.myvers.domain.Gender;
import com.example.myvers.domain.Mbti;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendForm {

    @NotBlank(message = "이름를 입력해주세요")
    @Size(min=1, max=8, message = "길이가 맞지 않습니다")
    private String name;

    @NotNull(message = "나이를 입력해주세요")
    @Min(value= 10, message = "Age must be at least 10")
    @Max(value= 99, message = "Age must be less than 100")
    private int age;

    @NotNull(message = "성별을 선택해주세요")
    private Gender gender;

    @NotNull(message = "MBTI를 선택해주세요")
    private Mbti mbti;

    @Builder
    public FriendForm(String name, int age, Gender gender, Mbti mbti) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.mbti = mbti;
    }
}
