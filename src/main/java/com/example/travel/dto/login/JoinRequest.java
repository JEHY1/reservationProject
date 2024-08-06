package com.example.travel.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinRequest {

    private String username;
    private String userPassword;
    private String userRealName;
    private String userPhone1;
    private String userPhone2;
    private String userPhone3;
    private String userEmail;
    private Boolean userSmsAgreement;
    private Boolean userEmailAgreement;

}
