package com.example.travel.dto.order;

import lombok.Data;

@Data
public class OrderStatusAndCountDto {

    public String orderStatus;
    private long orderStatusCount;

    public OrderStatusAndCountDto(String orderStatus, long orderStatusCount) {
        this.orderStatus = orderStatus;
        this.orderStatusCount = orderStatusCount;
    }
}
