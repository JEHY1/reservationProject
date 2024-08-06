package com.example.travel.dto.product;

import com.example.travel.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDetailInfoResponse {

    private Product product;
    private String today;

    @Builder
    public ProductDetailInfoResponse(Product product, String today) {
        this.product = product;
        this.today = today;
    }
}
