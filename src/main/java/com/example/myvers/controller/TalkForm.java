package com.example.myvers.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TalkForm {
    @NotBlank(message = "메시지를 입력하세요")
    @Size(max=100, message = "메시지는 100글자 이내로 보내주세요.")
    private String message;

    @Builder
    public TalkForm(String message) {
        this.message = message;
    }

}
