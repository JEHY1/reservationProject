package com.example.travel.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockResponse {
    private Long productId;
    private String date;
    private long count;

    @Builder
    public StockResponse(Long productId, String date, long count) {
        this.productId = productId;
        this.date = date;
        this.count = count;
    }
}
