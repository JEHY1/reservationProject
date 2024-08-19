package com.example.travel.dto.qna;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaSubmitRequest {

    private long productId;
    private String qnaQuestion;
    private boolean qnaSecret;
}
