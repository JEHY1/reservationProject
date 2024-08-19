package com.example.travel.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QnaAnswerRequest {

    private Long qnaId;
    private String qnaAnswer;

}
