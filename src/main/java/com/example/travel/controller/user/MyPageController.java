package com.example.travel.controller.user;

import com.example.travel.domain.Review;
import com.example.travel.dto.review.ReviewSubmitRequest;
import com.example.travel.service.PaymentService;
import com.example.travel.service.QnaService;
import com.example.travel.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final ReviewService reviewService;
    private final PaymentService paymentService;
    private final QnaService qnaService;

    @GetMapping("/myPage/reviewList")
    public String myReviewList(Model model, @RequestParam(defaultValue = "0") int page1, @RequestParam(defaultValue = "0") int page2, @RequestParam(required = false, defaultValue = "0") int tab, Principal principal){
        Pageable pageable1 = PageRequest.of(page1, 10);
        Pageable pageable2 = PageRequest.of(page2, 10);

        model.addAttribute("myWritableReviewPage", paymentService.myWritableReviewList(principal, pageable1));
        model.addAttribute("myWrittenReviewPage", paymentService.myWrittenReviewList(principal, pageable2));

        return "/myPage/myReviewList";
    }

    //리뷰 등록 페이지
    @GetMapping("/myPage/reviewPost")
    public String showReviewForm(@RequestParam(required = false) Long orderId, @RequestParam(required = false) Long reviewId, Model model) {

        if(reviewId != null){
            model.addAttribute("review", reviewService.findByReviewId(reviewId));
        }
        model.addAttribute("orderId", orderId);
        return "/myPage/reviewSubmit";
    }

    @PostMapping("/myPage/reviewPost")
    public String reviewPost(ReviewSubmitRequest request){
        reviewService.saveReview(request);
        return "redirect:/myPage/reviewList?tab=1";
    }

    @GetMapping("/myPage")
    public String myPage(Principal principal, Model model){

        model.addAttribute("orderHistory", paymentService.myOrderHistory(principal));

        return "/myPage/myPage";
    }

    @GetMapping("/myPage/qnaList")
    public String qnaList(Principal principal, Model model, @PageableDefault(page = 0, size = 10) Pageable pageable){

        model.addAttribute("qnaPage", qnaService.findQnaByPrincipalWithPage(principal, pageable));

        return "/myPage/qnaList";
    }

    @GetMapping("/myPage/qna/{qnaId}")
    public String qna(Model model, @PathVariable long qnaId){
        model.addAttribute("qna", qnaService.findById(qnaId));

        return "/myPage/qna";
    }
}
