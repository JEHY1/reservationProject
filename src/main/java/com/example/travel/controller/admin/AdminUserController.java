package com.example.travel.controller.admin;

import com.example.travel.domain.User;
import com.example.travel.dto.admin.AdminUserRequest;
import com.example.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminUserController {

    final private UserService userService;

    // admin_user_edit 회원정보 수정 페이지
    @GetMapping("/admin/user/{userId}/edit")
    public String editAdminUser(@PathVariable Long userId, Model model) {
        User userEntity = userService.findById(userId);
        model.addAttribute("user", userEntity);

        return "admins/admin_user_edit";
    }

    // admin_user_edit 회원정보 수정 post
    @PostMapping("/admin/user/update")
    public String updateAdminUser(AdminUserRequest request, RedirectAttributes rttr) {
        userService.updateAdminUser(request);
        rttr.addFlashAttribute("msg", "회원정보가 수정 되었습니다.");

        return "redirect:/admin/user/" + request.getUserId() + "/show";
    }

    // admin_user_show 회원정보 보기 페이지
    @GetMapping("/admin/user/{userId}/show")
    public String showAdminUser(@PathVariable Long userId, Model model) {
        User userEntity = userService.findById(userId);
        model.addAttribute("user", userEntity);

        return "admins/admin_user_show";
    }

    // admin_user_index 회원 목록 페이지
    @GetMapping("/admin/user/index")                           // default 페이지, 한 페이지 게시글 수, 정렬기준 컬럼, 정렬순서
    public String indexAdminUser(Model model, @PageableDefault(page = 0, size = 10) @SortDefault.SortDefaults({@SortDefault(sort = "userRealName", direction = Sort.Direction.ASC), @SortDefault(sort = "userId", direction = Sort.Direction.DESC)}) Pageable pageable,
                                 @RequestParam(required = false) Integer isDel, @RequestParam(defaultValue = "1") Integer group, @RequestParam(defaultValue = "1") Integer category, @RequestParam(defaultValue = "") String searchKeyword) {
        model.addAttribute("group", group);
        model.addAttribute("category", category);
        model.addAttribute("searchKeyword", searchKeyword);

        Page<User> userList = userService.userList(group, category, searchKeyword, pageable);
        model.addAttribute("users", userList);

        // 페이징 관련 변수
        int nowPage = userList.getPageable().getPageNumber()+1; // 현재 페이지 (pageable이 갖고 있는 페이지는 0부터이기 때문에 +1)
        int block = (int) Math.ceil(nowPage/5.0); // 페이지 구간 (5페이지 - 1구간)
        int startPage = (block - 1) * 5 + 1; // 블럭에서 보여줄 시작 페이지
        int lastPage = userList.getTotalPages() == 0 ? 1 : userList.getTotalPages(); // 존재하는 마지막 페이지
        int endPage = Math.min(startPage + 4, lastPage); // 블럭에서 보여줄 마지막 페이지
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 체크해서 탈퇴 했을 때 팝업창
        if(isDel != null && isDel == 1) {
            model.addAttribute("msg", "회원이 탈퇴 되었습니다.");
        }
        // 체크 안하고 탈퇴 눌렀을 때 팝업창
        if(isDel != null && isDel == 0) {
            model.addAttribute("msg", "탈퇴할 회원을 선택해주세요.");
        }

        return "admins/admin_user_index";
    }

    // 회원 탈퇴
    @GetMapping("/admin/user/{userId}/delete")
    public String deleteAdminUser(@PathVariable Long userId, RedirectAttributes rttr, Integer page, Integer group, Integer category, String searchKeyword) throws UnsupportedEncodingException {
        if(userService.findById(userId).getUserDeleteDate() == null) {
            userService.deleteUser(userId);
            rttr.addFlashAttribute("msg", "회원이 탈퇴 되었습니다.");
        }
        else {
            rttr.addFlashAttribute("msg", "이미 탈퇴된 회원입니다.");
        }
        // ASCII 아닌 파라미터 percent encoding
        String encodeSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
        return "redirect:/admin/user/index?page=" + page + "&group=" + group + "&category=" + category + "&searchKeyword=" + encodeSearchKeyword;
    }

    // 회원 선택 탈퇴 - admin_user_index.js 연결
    @PostMapping("/admin/user/delete")
    @ResponseBody
    public String deleteAdminUserSelect(@RequestParam(required = false) List<Long> userIds) {
        if(userIds != null) {
            userService.deleteByIds(userIds);
            return "delete";
        }
        else {
            return "null";
        }
    }

    // 회원정보 수정 시 기존 연락처 및 중복 체크 - user_information_modify.js에 ajax 연결
    @PostMapping("/admin/phone/check")
    @ResponseBody
    public String adminPhoneReCheck(String userPhone, Long userId) {

        System.out.println("휴대폰번호 확인: " + userPhone);

        return userService.phoneReCheck(userPhone, userId);
    }

    // 회원정보 수정 시 기존 이메일 및 중복 체크 - user_information_modify.js에 ajax 연결
    @PostMapping("/admin/email/check")
    @ResponseBody
    public String adminEmailReCheck(String userEmail, Long userId) {

        System.out.println("이메일 확인: " + userEmail);

        return userService.emailReCheck(userEmail, userId);
    }
}
