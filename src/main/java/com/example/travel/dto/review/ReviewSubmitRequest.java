package com.example.travel.dto.review;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class ReviewSubmitRequest {
    private Long reviewId;
    private Long orderId;
    private String reviewTitle;
    private String reviewContent;
    private LocalDate reviewSubmitDate;
    private int reviewScore;
    private List<MultipartFile> files;
}
