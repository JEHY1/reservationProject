package com.example.travel.controller.admin;

import com.example.travel.domain.Qna;
import com.example.travel.dto.login.QnaAnswerRequest;
import com.example.travel.service.QnaService;
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
public class AdminQnaController {

    private final QnaService qnaService;
    private final UserService userService;

    // admin_qna_insert 상품문의/답변 등록&수정 페이지
    @GetMapping(value = {"/admin/qna/{qnaId}/insert", "/seller/qna/{qnaId}/insert"})
    public String insertAdminQna(@PathVariable Long qnaId, Principal principal, Model model) {
        Qna qnaEntity = qnaService.findById(qnaId);
        model.addAttribute("qna", qnaEntity);
        // 유저 권한 가져옴
        model.addAttribute("auth", userService.getUserByPrincipal(principal).getUserRole().toLowerCase());

        return "admins/admin_qna_insert";
    }

    // admin_qna_insert 상품문의/답변 등록&수정 post
    @PostMapping(value = {"/admin/qna/create", "/seller/qna/create"})
    public String createAdminQna(QnaAnswerRequest request, Principal principal, RedirectAttributes rttr) {
        String beforeAnswer = qnaService.findById(request.getQnaId()).getQnaAnswer();
        Qna saved = qnaService.saveAnswer(request);
        if(beforeAnswer == null) {
            rttr.addFlashAttribute("msg", "문의답변이 등록 되었습니다.");
        }
        else {
            rttr.addFlashAttribute("msg", "문의답변이 수정 되었습니다.");
        }
        // 유저 권한 가져옴
        String auth = userService.getUserByPrincipal(principal).getUserRole().toLowerCase();

        return "redirect:/" + auth + "/qna/" + saved.getQnaId() + "/show";
    }

    // admin_qna_show 상품문의/답변 보기 페이지
    @GetMapping(value = {"/admin/qna/{qnaId}/show", "/seller/qna/{qnaId}/show"})
    public String showAdminQna(@PathVariable Long qnaId, Principal principal, Model model) {
        Qna qnaEntity = qnaService.findById(qnaId);
        model.addAttribute("qna", qnaEntity);
        // 유저 권한 가져옴
        model.addAttribute("auth", userService.getUserByPrincipal(principal).getUserRole().toLowerCase());

        return "admins/admin_qna_show";
    }




    // admin_qna_index 상품문의 목록 페이지
    @GetMapping(value = {"/admin/qna/index", "/seller/qna/index"})
    public String indexAdminQna(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable, @RequestParam(required = false) Integer isDel, @RequestParam(defaultValue = "1") Integer group, @RequestParam(defaultValue = "1") Integer category, @RequestParam(defaultValue = "") String searchKeyword, Principal principal) {
        // default 페이지, 한 페이지 게시글 수, 정렬기준 컬럼, 정렬순서
        model.addAttribute("group", group);
        model.addAttribute("category", category);
        model.addAttribute("searchKeyword", searchKeyword);

        // 유저 권한 가져옴
        model.addAttribute("auth", userService.getUserByPrincipal(principal).getUserRole().toLowerCase());

        Page<Qna> qnaList = qnaService.qnaList(group, category, searchKeyword, pageable);
        model.addAttribute("qnas", qnaList);


        // 페이징 관련 변수
        int nowPage = qnaList.getPageable().getPageNumber()+1; // 현재 페이지 (pageable이 갖고 있는 페이지는 0부터이기 때문에 +1)
        int block = (int) Math.ceil(nowPage/5.0); // 페이지 구간 (5페이지 - 1구간)
        int startPage = (block - 1) * 5 + 1; // 블럭에서 보여줄 시작 페이지
        int lastPage = qnaList.getTotalPages() == 0 ? 1 : qnaList.getTotalPages(); // 존재하는 마지막 페이지
        int endPage = Math.min(startPage + 4, lastPage); // 블럭에서 보여줄 마지막 페이지
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 체크해서 삭제 했을 때 팝업창
        if(isDel != null && isDel == 1) {
            model.addAttribute("msg", "상품문의가 삭제 되었습니다.");
        }
        // 체크 안하고 삭제 눌렀을 때 팝업창
        if(isDel != null && isDel == 0) {
            model.addAttribute("msg", "삭제할 상품문의를 선택해주세요.");
        }

        return "admins/admin_qna_index";
    }

    // 상품문의 삭제
    @GetMapping(value = {"/admin/qna/{qnaId}/delete", "/seller/qna/{qnaId}/delete"})
    public String deleteAdminQna(@PathVariable Long qnaId, RedirectAttributes rttr, Integer page, Integer group, Integer category, String searchKeyword, Principal principal) throws UnsupportedEncodingException {
        qnaService.deleteById(qnaId);
        rttr.addFlashAttribute("msg", "상품문의가 삭제 되었습니다.");
        // ASCII 아닌 파라미터 percent encoding
        String encodeSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
        // 유저 권한 가져옴
        String auth = userService.getUserByPrincipal(principal).getUserRole().toLowerCase();

        return "redirect:/" + auth + "/qna/index?page=" + page + "&group=" + group + "&category=" + category + "&searchKeyword=" + encodeSearchKeyword;
    }

    // 상품문의 선택 삭제 - admin_qna_index.js 연결
    @PostMapping(value = {"/admin/qna/delete", "/seller/qna/delete"})
    @ResponseBody
    public String deleteAdminQnASelect(@RequestParam(required = false) List<Long> qnaIds) {
        if(qnaIds != null) {
            qnaService.deleteByIds(qnaIds);
            return "delete";
        }
        else {
            return "null";
        }
    }

    // 문의답변 삭제
    @GetMapping(value = {"/admin/qna/{qnaId}/answer/delete", "/seller/qna/{qnaId}/answer/delete"})
    public String deleteAdminQnaAnswer(@PathVariable Long qnaId, Principal principal, RedirectAttributes rttr) {
        qnaService.deleteAnswer(qnaId);
        rttr.addFlashAttribute("msg", "문의답변이 삭제 되었습니다.");
        // 유저 권한 가져옴
        String auth = userService.getUserByPrincipal(principal).getUserRole().toLowerCase();

        return "redirect:/" + auth + "/qna/" + qnaId + "/insert";
    }

}
