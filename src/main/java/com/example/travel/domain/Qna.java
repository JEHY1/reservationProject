package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "qna_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id", updatable = false)
    private long qnaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "qna_question", nullable = false)
    private String qnaQuestion;

    @Column(name = "qna_answer")
    private String qnaAnswer;

    @CreatedDate
    @Column(name = "qna_submit_date", nullable = false)
    private LocalDateTime qnaSubmitDate;

    @Column(name = "qna_answer_date")
    private LocalDateTime qnaAnswerDate;

    @Column(name = "qna_views", nullable = false)
    private int qnaViews;

    @Column(name = "qna_secret", nullable = false)
    private boolean qnaSecret;

    public Qna(Product product, User user, String qnaQuestion, String qnaAnswer, LocalDateTime qnaSubmitDate, LocalDateTime qnaAnswerDate, int qnaViews, boolean qnaSecret) {
        this.product = product;
        this.user = user;
        this.qnaQuestion = qnaQuestion;
        this.qnaAnswer = qnaAnswer;
        this.qnaSubmitDate = qnaSubmitDate;
        this.qnaAnswerDate = qnaAnswerDate;
        this.qnaViews = qnaViews;
        this.qnaSecret = qnaSecret;
    }
}
