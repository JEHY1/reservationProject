package com.example.travel.controller.user;

import com.example.travel.domain.User;
import com.example.travel.dto.login.UpdatePasswordRequest;
import com.example.travel.dto.login.UpdateUserRequest;
import com.example.travel.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;

    // user_information_confirm 페이지 - 회원정보 수정 시 비밀번호 재확인
    @GetMapping("/user/information/confirm/new")
    public String newUserInformationConfirm(Principal principal, Model model) {
        User userEntity = userService.getUserByPrincipal(principal);
        model.addAttribute("user", userEntity);
        return "users/user_information_confirm";
    }

    // user_information_confirm 페이지 post
    @PostMapping("/user/information/confirm/create")
    public String createUserInformationConfirm() {
        return "redirect:/user/information/modify/new";
    }

    // user_information_modify 페이지 - 회원정보 수정
    @GetMapping("/user/information/modify/new")
    public String showUserInformationModify(Principal principal, Model model) {
        User userEntity = userService.getUserByPrincipal(principal);
        model.addAttribute("user", userEntity);
        return "users/user_information_modify";
    }

    @PostMapping("/user/information/modify/password") // user_information_modify 페이지 비밀번호 변경 post
    public String passwordUserInformationModify(UpdatePasswordRequest request, Principal principal, RedirectAttributes rttr) {
        userService.updatePassword(request, principal); // 비밀번호 변경
        rttr.addFlashAttribute("pwChange", "비밀번호가 변경되었습니다.");
        return "redirect:/user/information/modify/new";
    }
    @PostMapping("/password/check") // 기존 비밀번호 일치 여부 확인 - user_information_confirm/modify.js, user_delete_confirm.js에 ajax 연결
    @ResponseBody
    public String passwordUserInformationModifyCheck(String userPassword, Principal principal) {
        return userService.pwCheck(userPassword, principal);
    }

    @PostMapping("/user/information/modify/create") // user_information_modify 페이지 회원정보 수정 post
    public String createUserInformationModify(UpdateUserRequest request, Principal principal, RedirectAttributes rttr) {
        userService.updateUser(request, principal);
        rttr.addFlashAttribute("msg", "회원정보가 수정 되었습니다.");
        return "redirect:/user/information/modify/new";
    }

    @PostMapping("/phone/check") // 회원정보 수정 시 기존 연락처 및 중복 체크 - user_information_modify.js에 ajax 연결
    @ResponseBody
    public String phoneReCheck(String userPhone, Principal principal) {

        System.out.println("휴대폰번호 확인: " + userPhone);

        return userService.phoneReCheck(userPhone, principal);
    }
    @PostMapping("/email/check") // 회원정보 수정 시 기존 이메일 및 중복 체크 - user_information_modify.js에 ajax 연결
    @ResponseBody
    public String emailReCheck(String userEmail, Principal principal) {

        System.out.println("이메일 확인: " + userEmail);

        return userService.emailReCheck(userEmail, principal);
    }

    @GetMapping("/user/delete/confirm/new") // user_delete_confirm 페이지 - 회원 탈퇴 시 비밀번호 재확인
    public String newUserDeleteConfirm(Principal principal, Model model) {
        User userEntity = userService.getUserByPrincipal(principal);
        model.addAttribute("user", userEntity);
        return "users/user_delete_confirm";
    }
    @PostMapping("/user/delete/confirm/create") // user_delete_confirm 페이지 post
    public String createUserDeleteConfirm(Principal principal, HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(principal);
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/user/delete/finish";
    }

    @GetMapping("/user/delete/finish") // user_delete_finish 페이지 - 회원 탈퇴 완료
    public String showUserDeleteFinish() {
        return "users/user_delete_finish";
    }

}

