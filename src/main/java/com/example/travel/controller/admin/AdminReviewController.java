package com.example.travel.controller.admin;

import com.example.travel.domain.Review;
import com.example.travel.domain.ReviewImg;
import com.example.travel.dto.admin.ReviewCommentRequest;
import com.example.travel.service.ReviewService;
import com.example.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class AdminReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    // admin_review_insert 리뷰/답글 등록&수정 페이지
    @GetMapping(value = {"/admin/review/{reviewId}/insert", "/seller/review/{reviewId}/insert"})
    public String insertAdminReview(@PathVariable Long reviewId, Principal principal, Model model) {
        Review reviewEntity = reviewService.findByReviewId(reviewId);
        model.addAttribute("review", reviewEntity);
        List<ReviewImg> reviewImgList = reviewService.FindReviewImgsByReviewId(reviewId);
        model.addAttribute("reviewImgs", reviewImgList);
        // 유저 권한 가져옴
        model.addAttribute("auth", userService.getUserByPrincipal(principal).getUserRole().toLowerCase());

        return "admins/admin_review_insert";
    }

    // admin_review_insert 리뷰/답글 등록&수정 post
    @PostMapping(value = {"/admin/review/create", "/seller/review/create"})
    public String createAdminReview(ReviewCommentRequest request, Principal principal, RedirectAttributes rttr) {
        String beforeComment = reviewService.findByReviewId(request.getReviewId()).getReviewComment();
        Review saved = reviewService.saveComment(request);
        if(beforeComment == null) {
            rttr.addFlashAttribute("msg", "리뷰답글이 등록 되었습니다.");
        }
        else {
            rttr.addFlashAttribute("msg", "리뷰답글이 수정 되었습니다.");
        }
        // 유저 권한 가져옴
        String auth = userService.getUserByPrincipal(principal).getUserRole().toLowerCase();

        return "redirect:/" + auth + "/review/" + saved.getReviewId() + "/show";
    }

    // admin_review_show 리뷰/답글 보기 페이지
    @GetMapping(value = {"/admin/review/{reviewId}/show", "/seller/review/{reviewId}/show"})
    public String showAdminReview(@PathVariable Long reviewId, Principal principal, Model model) {
        Review reviewEntity = reviewService.findByReviewId(reviewId);
        model.addAttribute("review", reviewEntity);
        List<ReviewImg> reviewImgList = reviewService.FindReviewImgsByReviewId(reviewId);
        model.addAttribute("reviewImgs", reviewImgList);
        // 유저 권한 가져옴
        model.addAttribute("auth", userService.getUserByPrincipal(principal).getUserRole().toLowerCase());

        return "admins/admin_review_show";
    }

    // admin_review_index 리뷰 목록 페이지
    @GetMapping(value = {"/admin/review/index", "/seller/review/index"})
    public String indexAdminReview(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable, @RequestParam(required = false) Integer isDel, @RequestParam(defaultValue = "1") Integer category, @RequestParam(defaultValue = "") String searchKeyword, Principal principal) {
        // default 페이지, 한 페이지 게시글 수, 정렬기준 컬럼
        model.addAttribute("category", category);
        model.addAttribute("searchKeyword", searchKeyword);

        // 유저 권한 가져옴
        model.addAttribute("auth", userService.getUserByPrincipal(principal).getUserRole().toLowerCase());

        Page<Review> reviewList = reviewService.reviewList(category, searchKeyword, pageable);
        model.addAttribute("reviews", reviewList);

        // 페이징 관련 변수
        int nowPage = reviewList.getPageable().getPageNumber()+1; // 현재 페이지 (pageable이 갖고 있는 페이지는 0부터이기 때문에 +1)
        int block = (int) Math.ceil(nowPage/5.0); // 페이지 구간 (5페이지 - 1구간)
        int startPage = (block - 1) * 5 + 1; // 블럭에서 보여줄 시작 페이지
        int lastPage = reviewList.getTotalPages() == 0 ? 1 : reviewList.getTotalPages(); // 존재하는 마지막 페이지
        int endPage = Math.min(startPage + 4, lastPage); // 블럭에서 보여줄 마지막 페이지
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 체크해서 삭제 했을 때 팝업창
        if(isDel != null && isDel == 1) {
            model.addAttribute("msg", "리뷰가 삭제 되었습니다.");
        }
        // 체크 안하고 삭제 눌렀을 때 팝업창
        if(isDel != null && isDel == 0) {
            model.addAttribute("msg", "삭제할 리뷰를 선택해주세요.");
        }

        return "admins/admin_review_index";
    }

    // 리뷰 삭제
    @GetMapping(value = {"/admin/review/{reviewId}/delete", "/seller/review/{reviewId}/delete"})
    public String deleteAdminReview(@PathVariable Long reviewId, RedirectAttributes rttr, Integer page, Integer category, String searchKeyword, Principal principal) throws UnsupportedEncodingException {
        reviewService.deleteReviewByReviewId(reviewId);
        rttr.addFlashAttribute("msg", "리뷰가 삭제 되었습니다.");
        // ASCII 아닌 파라미터 percent encoding
        String encodeSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
        // 유저 권한 가져옴
        String auth = userService.getUserByPrincipal(principal).getUserRole().toLowerCase();

        return "redirect:/" + auth + "/review/index?page=" + page + "&category=" + category + "&searchKeyword=" + encodeSearchKeyword;
    }

    // 리뷰 선택 삭제 - admin_review_index.js 연결
    @PostMapping(value = {"/admin/review/delete", "/seller/review/delete"})
    @ResponseBody
    public String deleteAdminReviewSelect(@RequestParam(required = false) List<Long> reviewIds) {
        if(reviewIds != null) {
            reviewService.deleteReviewsByReviewIds(reviewIds);
            return "delete";
        }
        else {
            return "null";
        }
    }

    // 리뷰답글 삭제
    @GetMapping(value = {"/admin/review/{reviewId}/comment/delete", "/seller/review/{reviewId}/comment/delete"})
    public String deleteAdminReviewComment(@PathVariable Long reviewId, Principal principal, RedirectAttributes rttr) {
        reviewService.deleteComment(reviewId);
        rttr.addFlashAttribute("msg", "리뷰답글이 삭제 되었습니다.");
        // 유저 권한 가져옴
        String auth = userService.getUserByPrincipal(principal).getUserRole().toLowerCase();

        return "redirect:/" + auth + "/review/" + reviewId + "/insert";
    }

}
