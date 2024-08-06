package com.example.travel.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockRequest {
    private long productId;
    private int year;
    private int month;
    private int day;
}
