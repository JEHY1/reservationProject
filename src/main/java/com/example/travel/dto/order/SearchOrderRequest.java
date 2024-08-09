package com.example.travel.dto.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class SearchOrderRequest {
    private Long productId;
    private LocalDate orderDate1;
    private LocalDate orderDate2;
    private LocalDate reservationDate1;
    private LocalDate reservationDate2;
    private String paymentType;
    private String depositorName;
    private Integer paymentPrice1;
    private Integer paymentPrice2;
    private String orderStatus;
}
