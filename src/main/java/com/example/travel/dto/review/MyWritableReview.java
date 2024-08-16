package com.example.travel.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyWritableReview {
    private long orderId;
    private long productId;
    private LocalDateTime orderDepartureDate;
    private LocalDateTime orderEndDate;
    private String productRepImg;
    private String productName;
    private LocalDateTime deadLine;

    @Builder
    public MyWritableReview(long orderId, long productId, LocalDateTime orderDepartureDate, LocalDateTime orderEndDate, String productRepImg, String productName, LocalDateTime deadLine) {
        this.orderId = orderId;
        this.productId = productId;
        this.orderDepartureDate = orderDepartureDate;
        this.orderEndDate = orderEndDate;
        this.productRepImg = productRepImg;
        this.productName = productName;
        this.deadLine = deadLine;
    }
}
