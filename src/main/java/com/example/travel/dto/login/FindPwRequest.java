package com.example.travel.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindPwRequest {

    private String username;
    private String userRealName;
    private String userEmail;
}
