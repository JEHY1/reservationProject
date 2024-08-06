package com.example.travel.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateUserRequest {

    private String userPhone;
    private String userEmail;
    private Boolean userSmsAgreement;
    private Boolean userEmailAgreement;

}
