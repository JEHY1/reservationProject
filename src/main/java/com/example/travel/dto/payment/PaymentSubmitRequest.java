package com.example.travel.dto.payment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentSubmitRequest {

    private long orderId;
    private String paymentRefundAccount;
    private String paymentRefundAccountName;
}
