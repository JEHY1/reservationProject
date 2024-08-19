package com.example.travel.dto.login;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginCheckResponse {

    private final String message = "로그인 여부";
    private boolean value;

    @Builder
    public LoginCheckResponse(boolean value) {
        this.value = value;
    }
}