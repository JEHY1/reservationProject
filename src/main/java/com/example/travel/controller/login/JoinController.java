package com.example.travel.controller.login;

import com.example.travel.domain.User;
import com.example.travel.dto.login.JoinRequest;
import com.example.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;

    // join 페이지
    @GetMapping("/join/new")
    public String newJoin() {
        return "logins/join";
    }

    // join 페이지 post
    @PostMapping("/join/create")
    public String createJoin(JoinRequest request) {
        Long userId = userService.save(request);
        return "redirect:/join/" + userId + "/finish";
    }

    // 아이디 중복 확인 - join.js에 ajax 연결
    @PostMapping("/join/idCheck")
    @ResponseBody
    public String idCheck(String username) {

        System.out.println("아이디 확인: " + username);

        return userService.idCheck(username);
    }

    // 연락처 중복 확인 - join.js에 ajax 연결
    @PostMapping("/join/phoneCheck")
    @ResponseBody
    public String phoneCheck(String userPhone) {

        System.out.println("휴대폰번호 확인: " + userPhone);

        return userService.phoneCheck(userPhone);
    }

    // 이메일 중복 확인 - join.js에 ajax 연결
    @PostMapping("/join/emailCheck")
    @ResponseBody
    public String emailCheck(String userEmail) {

        System.out.println("이메일 확인: " + userEmail);

        return userService.emailCheck(userEmail);
    }

    // join_finish 페이지
    @GetMapping("/join/{userId}/finish")
    public String showJoinFinish(@PathVariable Long userId, Model model) {
        User userEntity = userService.findById(userId);
        model.addAttribute("user", userEntity);
        return "logins/join_finish";
    }

}
