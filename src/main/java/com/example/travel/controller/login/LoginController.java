package com.example.travel.controller.login;

import com.example.travel.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    // login 페이지
    @GetMapping("/login")
    public String login() {
        return "logins/login";
    }

    // 로그인 시 아이디, 비밀번호 확인 - login.js에 ajax 연결
    @PostMapping("/login/check")
    @ResponseBody
    public String loginCheck(String username, String userPassword) {

        System.out.println("아이디 확인: " + username);
        System.out.println("비밀번호 확인: " + userPassword);

        return userService.loginCheck(username, userPassword);
    }

    // logout 시키기
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

}
