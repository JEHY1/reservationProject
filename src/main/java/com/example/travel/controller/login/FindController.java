package com.example.travel.controller.login;


import com.example.travel.domain.User;
import com.example.travel.dto.login.FindIdRequest;
import com.example.travel.dto.login.FindPwRequest;
import com.example.travel.dto.login.ResetPasswordRequest;
import com.example.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class FindController {

    private final UserService userService;

    // find_id 페이지
    @GetMapping("/find/id/new")
    public String newFindId() {
        return "logins/find_id";
    }

    // find_id 페이지 post
    @PostMapping("find/id/create")
    public String createFindId(FindIdRequest request, RedirectAttributes rttr) {
        User userEntity = userService.findByUserEmail(request.getUserEmail());
        if(userEntity != null && userEntity.getUserDeleteDate() == null && request.getUserRealName().equals(userEntity.getUserRealName())) {
            return "redirect:/find/id/" + userEntity.getUserId() + "/finish";
        }
        else {
            rttr.addFlashAttribute("msg", "아이디를 찾을 수 없습니다.");
            return "redirect:/find/id/new";
        }
    }

    // find_id_finish 페이지
    @GetMapping("/find/id/{userId}/finish")
    public String showFindIdFinish(@PathVariable Long userId, Model model) {
        User userEntity = userService.findById(userId);
        model.addAttribute("user", userEntity);
        return "logins/find_id_finish";
    }

    // find_pw 페이지
    @GetMapping("/find/pw/new")
    public String newFindPw() {
        return "logins/find_pw";
    }

    // find_pw 페이지 post
    @PostMapping("/find/pw/create")
    public String createFindPw(FindPwRequest request, RedirectAttributes rttr) {
        User userEntity = userService.findByUserEmail(request.getUserEmail());
        if(userEntity != null && userEntity.getUserDeleteDate() == null && request.getUsername().equals(userEntity.getUsername()) && request.getUserRealName().equals(userEntity.getUserRealName())) {
            return "redirect:/find/pw/" + userEntity.getUserId() + "/finish/new";
        }
        else {
            rttr.addFlashAttribute("msg", "사용자 정보가 일치하지 않습니다.");
            return "redirect:/find/pw/new";
        }

    }

    // find_pw_finish 페이지
    @GetMapping("/find/pw/{userId}/finish/new")
    public String newFindPwFinish(@PathVariable Long userId, Model model) {
        User userEntity = userService.findById(userId);
        model.addAttribute("user", userEntity);
        return "logins/find_pw_finish";
    }

    // find_pw_finish 페이지 post
    @PostMapping("/find/pw/finish/create")
    public String createFindPwFinish(ResetPasswordRequest request, RedirectAttributes rttr) {
        // 비밀번호 재설정
        userService.resetPassword(request);
        rttr.addFlashAttribute("msg", "비밀번호가 재설정 되었습니다.");
        return "redirect:/login";
    }

}
