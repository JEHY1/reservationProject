package com.example.travel.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyWrittenReviewResponse {

    private List<MyWrittenReview> myWrittenReviewList;
    private int totalPages;
    private int totalElements;

    @Builder
    public MyWrittenReviewResponse(List<MyWrittenReview> myWrittenReviewList, int totalPages, int totalElements) {
        this.myWrittenReviewList = myWrittenReviewList;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
