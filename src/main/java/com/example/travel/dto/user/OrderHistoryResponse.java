package com.example.travel.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderHistoryResponse {

    private int nonDeposit = 0;
    private int depositNonCheck = 0;
    private int paymentComplete = 0;
    private int orderCancel = 0;
    private int refunding = 0;
    private int refundComplete = 0;

    @Builder
    public OrderHistoryResponse(int nonDeposit, int depositNonCheck, int paymentComplete, int orderCancel, int refunding, int refundComplete) {
        this.nonDeposit = nonDeposit;
        this.depositNonCheck = depositNonCheck;
        this.paymentComplete = paymentComplete;
        this.orderCancel = orderCancel;
        this.refunding = refunding;
        this.refundComplete = refundComplete;
    }
}
