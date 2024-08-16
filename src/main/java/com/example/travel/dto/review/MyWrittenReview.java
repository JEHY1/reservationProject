package com.example.travel.dto.review;

import com.example.travel.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyWrittenReview {
    private long reviewId;
    private String productName;
    private String reviewTitle;
    private String reviewContent;
    private LocalDateTime reviewCreateDate;
    private int reviewScore;
    private String reviewRepImg;
    private String productRepImg;
    private boolean isReviewUpdatable;

    @Builder
    public MyWrittenReview(Review review, String productName, String reviewRepImg, String productRepImg, boolean isReviewUpdatable) {
        this.reviewId = review.getReviewId();
        this.productName = productName;
        this.reviewTitle = review.getReviewTitle();
        this.reviewContent = review.getReviewTitle();
        this.reviewCreateDate = review.getReviewSubmitDate();
        this.reviewScore = review.getReviewScore();
        this.reviewRepImg = reviewRepImg;
        this.productRepImg = productRepImg;
        this.isReviewUpdatable = isReviewUpdatable;
    }
}
