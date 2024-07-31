package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_option_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id", updatable = false)
    private long productOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_option_age_range", nullable = false)
    private String productOptionAgeRange;

    @Column(name = "product_option_regular_price", nullable = false)
    private int productOptionRegularPrice;

    @Column(name = "product_option_discount_price")
    private Integer productOptionDiscountPrice;

    @Builder
    public ProductOption(Product product, String productOptionAgeRange, int productOptionRegularPrice, Integer productOptionDiscountPrice) {
        this.product = product;
        this.productOptionAgeRange = productOptionAgeRange;
        this.productOptionRegularPrice = productOptionRegularPrice;
        this.productOptionDiscountPrice = productOptionDiscountPrice;
    }
}
