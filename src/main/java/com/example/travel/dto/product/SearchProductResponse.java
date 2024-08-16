package com.example.travel.dto.product;

import com.example.travel.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SearchProductResponse {

    private List<Product> productList;
    private int totalPages;
    private int totalElements;

    @Builder
    public SearchProductResponse(List<Product> productList, int totalPages, int totalElements) {
        this.productList = productList;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
