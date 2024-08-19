package com.example.travel.dto.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class ProductRequest {

    private Long productId;
    private String productStatus;
    private String productRegionMainCategory;
    private String productRegionSubCategory;
    private String productTitle;
    private String productStartDate;
    private String productEndDate;
    private int productRegularPrice;
    private Integer productDiscountPrice;
    private int productTravelDays;
    private int productMaxCount;
    private String productInfo;
    private List<MultipartFile> productRepImg;
    private List<MultipartFile> productInfoImg;
    private List<ProductOptionRequest> productOptions;


}
