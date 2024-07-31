package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "product_info_img_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ProductInfoImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_info_img_id", updatable = false)
    private long productInfoImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_img_src")
    private String productImgSrc;

    @Builder
    public ProductInfoImg(Product product, String productImgSrc) {
        this.product = product;
        this.productImgSrc = productImgSrc;
    }
}
