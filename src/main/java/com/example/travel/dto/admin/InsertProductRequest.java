package com.example.travel.dto.admin;

import com.example.travel.domain.ProductOption;
import com.example.travel.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class InsertProductRequest {

    private Long productId;
    private String productStatus;
    private String productRegionMainCategory;
    private String productRegionSubCategory;
    private String productTitle;
    private String productStartDate;
    private String productEndDate;
    private int productRegularPrice;
    private Integer productDiscountPrice;
    private int productMaxCount;
    private String productInfo;
    private List<MultipartFile> productRepImg;
    private List<MultipartFile> productInfoImg;
    private List<InsertProductOptionRequest> productOptions;

}
