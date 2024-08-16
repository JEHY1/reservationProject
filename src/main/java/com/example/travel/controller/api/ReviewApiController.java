package com.example.travel.controller.api;

import com.example.travel.dto.review.ReviewControllerRequest;
import com.example.travel.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @DeleteMapping("/api/myPage/review")
    public ResponseEntity<Void> deleteReview(@RequestBody ReviewControllerRequest request){
        System.err.println("call delete Review");
        reviewService.deleteReviewByReviewId(request.getReviewId());

        return ResponseEntity.ok()
                .build();
    }
}
