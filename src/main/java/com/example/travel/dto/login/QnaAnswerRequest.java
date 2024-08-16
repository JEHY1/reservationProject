package com.example.travel.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QnaAnswerRequest {

    private Long qnaId;
    private String qnaAnswer;

}
