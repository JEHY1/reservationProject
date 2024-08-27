package com.example.travel.dto.order;

import lombok.Data;

@Data
public class OptionSellDto {
    public long productOptionId;
    public String productOptionName;
    public long totalSellCount;

    public OptionSellDto(long productOptionId, String productOptionName, long totalSellCount) {
        this.productOptionId = productOptionId;
        this.productOptionName = productOptionName;
        this.totalSellCount = totalSellCount;
    }
}
