package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review_img_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_img_id", updatable = false)
    private long reviewImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(name = "review_img_src", nullable = false)
    private String reviewImgSrc;

    @Builder
    public ReviewImg(Review review, String reviewImgSrc) {
        this.review = review;
        this.reviewImgSrc = reviewImgSrc;
    }
}
