package com.example.travel.domain;

import com.example.travel.dto.admin.ProductRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "product_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "product_status", nullable = false)
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

    @Column(name = "product_travel_days")
    private int productTravelDays;

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

    @Builder
    public Product(User user, String productTitle, int productRegularPrice, Integer productDiscountPrice, LocalDateTime productStartDate, LocalDateTime productEndDate, String productInfo, String productStatus, String productRegionMainCategory, String productRegionSubCategory, int productMaxCount, int productTravelDays) {
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
        this.productTravelDays = productTravelDays;
    }

    public Product updateProduct(ProductRequest dto) {

        this.productStatus = dto.getProductStatus();
        this.productRegionMainCategory = dto.getProductRegionMainCategory();
        this.productRegionSubCategory = dto.getProductRegionSubCategory();
        this.productTitle = dto.getProductTitle();
        this.productStartDate = LocalDateTime.parse(dto.getProductStartDate() + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.productEndDate = LocalDateTime.parse(dto.getProductEndDate() + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.productRegularPrice = dto.getProductRegularPrice();
        this.productDiscountPrice = dto. getProductDiscountPrice();
        this.productTravelDays = dto.getProductTravelDays();
        this.productMaxCount = dto.getProductMaxCount();
        this.productInfo = dto.getProductInfo();
        return this;
    }

    public void updateStatus(String productStatus){
        this.productStatus = productStatus;
    }

}
