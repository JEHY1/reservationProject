package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", updatable = false)
    private long reviewId;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "review_title", nullable = false)
    private String reviewTitle;

    @Column(name = "review_content", nullable = false)
    private String reviewContent;

    @Column(name = "review_comment")
    private String reviewComment;

    @Column(name = "review_views")
    private int reviewViews;

    @Builder
    public Review(Order order, String reviewTitle, String reviewContent, String reviewComment, int reviewViews) {
        this.order = order;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.reviewComment = reviewComment;
        this.reviewViews = reviewViews;
    }
}
