package com.example.travel.domain;

import com.example.travel.dto.review.ReviewSubmitRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "review_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", updatable = false)
    private long reviewId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "review_title", nullable = false)
    private String reviewTitle;

    @Column(name = "review_content", nullable = false)
    private String reviewContent;

    @CreatedDate
    @Column(name = "review_submit_date", nullable = false)
    private LocalDateTime reviewSubmitDate;

    @Column(name = "review_comment")
    private String reviewComment;

    @Column(name = "review_comment_submit_date")
    private LocalDateTime reviewCommentSubmitDate;

    @Column(name = "review_score", nullable = false)
    private int reviewScore;

    @ToString.Exclude
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewImg> ReviewImgList;

    @Builder
    public Review(Order order, Product product, String reviewTitle, String reviewContent, String reviewComment, int reviewScore) {
        this.order = order;
        this.product = product;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.reviewComment = reviewComment;
        this.reviewScore = reviewScore;
    }

    public Review updateReview(ReviewSubmitRequest request){
        this.reviewTitle = request.getReviewTitle();
        this.reviewContent = request.getReviewContent();
        this.reviewScore = request.getReviewScore();
        return this;
    }
}
