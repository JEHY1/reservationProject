package com.example.travel.controller.api;

import com.example.travel.dto.login.LoginCheckResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LoginApiController {

    @GetMapping("/api/checkLogin")
    public ResponseEntity<LoginCheckResponse> checkLogin(Principal principal){
        return ResponseEntity.ok()
                .body(LoginCheckResponse.builder()
                        .value(principal != null)
                        .build());
    }
}
