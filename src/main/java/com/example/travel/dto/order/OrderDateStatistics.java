package com.example.travel.dto.order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDateStatistics {
    private LocalDateTime date;
    private long departureCount;

    public OrderDateStatistics(LocalDateTime date, long departureCount) {
        this.date = date;
        this.departureCount = departureCount;
    }
}
