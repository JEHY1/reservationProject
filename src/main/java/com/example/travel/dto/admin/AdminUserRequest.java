package com.example.travel.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserRequest {

    private Long userId;
    private String userPassword;
    private String userRealName;
    private String userPhone;
    private String userEmail;
    private String userRole;
    private Boolean userSmsAgreement;
    private Boolean userEmailAgreement;
}
