package com.example.travel.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewCommentRequest {

    private Long reviewId;
    private String reviewComment;

}
