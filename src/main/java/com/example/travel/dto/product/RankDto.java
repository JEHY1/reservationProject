package com.example.travel.dto.product;

import lombok.Data;

@Data
public class RankDto {

    private long productId;
    private String productTitle;
    private Long totalRevenue;
    private Long totalOrderCount;
    private long rank;
    private Long changedRankCount;

    public RankDto(long productId, String productTitle, Long totalRevenue, Long totalOrderCount) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.totalRevenue = totalRevenue;
        this.totalOrderCount = totalOrderCount;
    }
}
