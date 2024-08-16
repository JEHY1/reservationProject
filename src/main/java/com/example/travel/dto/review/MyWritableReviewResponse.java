package com.example.travel.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class MyWritableReviewResponse {
    private List<MyWritableReview> myWritableReviewList;
    private int totalPages;
    private int totalElements;

    @Builder
    public MyWritableReviewResponse(List<MyWritableReview> myWritableReviewList, int totalPages, int totalElements) {
        this.myWritableReviewList = myWritableReviewList;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

}
