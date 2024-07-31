package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_rep_img_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ProductRepImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_rep_img_id", updatable = false)
    private long productRepImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_rep_img_src", nullable = false)
    private String productRepImgSrc;

    @Builder
    public ProductRepImg(Product product, String productRepImgSrc) {
        this.product = product;
        this.productRepImgSrc = productRepImgSrc;
    }
}
