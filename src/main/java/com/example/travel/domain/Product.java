package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", updatable = false)
    private long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "product_title", nullable = false)
    private String productTitle;

    @Column(name = "product_regular_price", nullable = false)
    private int productRegularPrice;

    @Column(name = "product_discount_price")
    private Integer productDiscountPrice;

    @Column(name ="product_start_date", nullable = false)
    private LocalDateTime productStartDate;

    @Column(name = "product_end_date", nullable = false)
    private LocalDateTime productEndDate;

    @Column(name = "product_info")
    private String productInfo;

    @Column(name = "product_status")
    private String productStatus;

    @CreatedDate
    @Column(name = "product_registration_date", nullable = false)
    private LocalDateTime productRegistrationDate;

    @Column(name = "product_region_main_category", nullable = false)
    private String productRegionMainCategory;

    @Column(name = "product_region_sub_category", nullable = false)
    private String productRegionSubCategory;

    @Column(name = "product_max_count", nullable = false)
    private int productMaxCount;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<ProductRepImg> productRepImgList;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<ProductOption> productOptionList;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<ProductInfoImg> productInfoImgList;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<Qna> QnaList;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<Review> reviewList;

    public Product(User user, String productTitle, int productRegularPrice, Integer productDiscountPrice, LocalDateTime productStartDate, LocalDateTime productEndDate, String productInfo, String productStatus, String productRegionMainCategory, String productRegionSubCategory, int productMaxCount) {
        this.user = user;
        this.productTitle = productTitle;
        this.productRegularPrice = productRegularPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.productStartDate = productStartDate;
        this.productEndDate = productEndDate;
        this.productInfo = productInfo;
        this.productStatus = productStatus;
        this.productRegionMainCategory = productRegionMainCategory;
        this.productRegionSubCategory = productRegionSubCategory;
        this.productMaxCount = productMaxCount;
    }
}
