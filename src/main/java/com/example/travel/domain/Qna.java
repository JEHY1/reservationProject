package com.example.travel.domain;

import com.example.travel.dto.admin.QnaAnswerRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "qna_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "qna_secret", nullable = false)
    private boolean qnaSecret;

    @Builder
    public Qna(Product product, User user, String qnaQuestion, String qnaAnswer, LocalDateTime qnaSubmitDate, LocalDateTime qnaAnswerDate, boolean qnaSecret) {
        this.product = product;
        this.user = user;
        this.qnaQuestion = qnaQuestion;
        this.qnaAnswer = qnaAnswer;
        this.qnaSubmitDate = qnaSubmitDate;
        this.qnaAnswerDate = qnaAnswerDate;
        this.qnaSecret = qnaSecret;
    }

    // 상품문의 답변 등록&수정
    public Qna saveAnswer(QnaAnswerRequest dto) {
        this.qnaAnswer = dto.getQnaAnswer();
        this.qnaAnswerDate = this.qnaAnswerDate == null ? LocalDateTime.now() : this.qnaAnswerDate;
        return this;
    }

    // 상품문의 답변 삭제
    public Qna deleteAnswer() {
        this.qnaAnswer = null;
        this.qnaAnswerDate = null;
        return this;
    }
}
