package com.example.travel.dto.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderSubmitRequest {

    private long productId;
    private String orderDepartDate;
    private List<Long> optionList;
    private List<Integer> countList;
    private List<Integer> totalOptionRegularPriceList;
    private List<Integer> totalOptionDiscountPriceList;
    private int totalPrice;
    private String userRealName;
    private String userPhone;
    private String userEmail;
    private String paymentType;
    private String account;
    private String depositor;
}
