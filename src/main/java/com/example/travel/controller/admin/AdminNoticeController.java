package com.example.travel.controller.admin;

import com.example.travel.domain.Notice;
import com.example.travel.domain.User;
import com.example.travel.dto.admin.NoticeRequest;
import com.example.travel.service.NoticeService;
import com.example.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
public class AdminNoticeController {

    private final NoticeService noticeService;
    private final UserService userService;

    // admin_notice_insert 공지사항 등록&수정 페이지
    @GetMapping(value = {"/admin/notice/insert", "/admin/notice/{noticeId}/insert"})
    public String insertAdminNotice(@PathVariable(required = false) Long noticeId, Principal principal, Model model) {
        // 로그인 된 User 정보
        User userEntity = userService.getUserByPrincipal(principal);
        model.addAttribute("user", userEntity);
        // 공지사항 수정
        if(noticeId != null) {
            Notice noticeEntity = noticeService.findById(noticeId);
            model.addAttribute("notice", noticeEntity);
        }

        return "admins/admin_notice_insert";
    }

    // admin_notice_insert 공지사항 등록&수정 post
    @PostMapping("/admin/notice/create")
    public String createAdminNotice(NoticeRequest request, Principal principal, RedirectAttributes rttr) {
        Notice saved = noticeService.save(request, principal);
        if(request.getNoticeId() == null) {
            rttr.addFlashAttribute("msg", "공지사항이 등록 되었습니다.");
        }
        else {
            rttr.addFlashAttribute("msg", "공지사항이 수정 되었습니다.");
        }

        return "redirect:/admin/notice/" + saved.getNoticeId() + "/show";
    }

    // admin_notice_insert 공지사항 보기 페이지
    @GetMapping("/admin/notice/{noticeId}/show")
    public String showAdminNotice(@PathVariable Long noticeId, Model model, Principal principal) {
        Notice noticeEntity = noticeService.findById(noticeId);
        model.addAttribute("notice", noticeEntity);

        return "admins/admin_notice_show";
    }

    // admin_notice_insert 공지사항 목록 페이지
    @GetMapping("/admin/notice/index")
    public String indexAdminNotice(Model model, @PageableDefault(page = 0, size = 10, sort = "noticeId", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) Integer isDel, @RequestParam(defaultValue = "1") Integer category, @RequestParam(defaultValue = "") String searchKeyword) {
        // default 페이지, 한 페이지 게시글 수, 정렬기준 컬럼, 정렬순서
        model.addAttribute("category", category);
        model.addAttribute("searchKeyword", searchKeyword);

        Page<Notice> noticeList = noticeService.noticeList(category, searchKeyword, pageable);
        model.addAttribute("notices", noticeList);

        // 페이징 관련 변수
        int nowPage = noticeList.getPageable().getPageNumber()+1; // 현재 페이지 (pageable이 갖고 있는 페이지는 0부터이기 때문에 +1)
        int block = (int) Math.ceil(nowPage/5.0); // 페이지 구간 (5페이지 - 1구간)
        int startPage = (block - 1) * 5 + 1; // 블럭에서 보여줄 시작 페이지
        int lastPage = noticeList.getTotalPages() == 0 ? 1 : noticeList.getTotalPages(); // 존재하는 마지막 페이지
        int endPage = Math.min(startPage + 4, lastPage); // 블럭에서 보여줄 마지막 페이지
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 체크해서 삭제 했을 때 팝업창
        if(isDel != null && isDel == 1) {
            model.addAttribute("msg", "공지사항이 삭제 되었습니다.");
        }
        // 체크 안하고 삭제 눌렀을 때 팝업창
        if(isDel != null && isDel == 0) {
            model.addAttribute("msg", "삭제할 공지사항을 선택해주세요.");
        }

        return "admins/admin_notice_index";
    }

    // 공지사항 삭제
    @GetMapping("/admin/notice/{noticeId}/delete")
    public String deleteAdminNotice(@PathVariable Long noticeId, RedirectAttributes rttr, Integer page, Integer category, String searchKeyword) throws UnsupportedEncodingException {
        noticeService.deleteById(noticeId);
        rttr.addFlashAttribute("msg", "공지사항이 삭제 되었습니다.");
        // ASCII 아닌 파라미터 percent encoding
        String encodeSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
        return "redirect:/admin/notice/index?page=" + page + "&category=" + category + "&searchKeyword=" + encodeSearchKeyword;
    }

    // 공지사항 선택 삭제 - admin_notice_index.js 연결
    @PostMapping("/admin/notice/delete")
    @ResponseBody
    public String deleteAdminNoticeSelect(@RequestParam(required = false) List<Long> noticeIds) {
        if(noticeIds != null) {
            noticeService.deleteByIds(noticeIds);
            return "delete";
        }
        else {
            return "null";
        }
    }
}
