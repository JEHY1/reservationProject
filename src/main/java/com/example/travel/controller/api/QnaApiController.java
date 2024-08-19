package com.example.travel.controller.api;

import com.example.travel.domain.Qna;
import com.example.travel.dto.qna.QnaSubmitRequest;
import com.example.travel.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class QnaApiController {

    private final QnaService qnaService;

    @PostMapping("/api/qnaSubmit")
    public ResponseEntity<Qna> qnaSubmit(@RequestBody QnaSubmitRequest request, Principal principal){
        System.err.println(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(qnaService.saveQna(request, principal));
    }
}
