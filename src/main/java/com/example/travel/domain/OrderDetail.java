package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_detail_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id", updatable = false)
    private long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order Order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    @Column(name = "payment_detail_traveler_count", nullable = false)
    private int paymentDetailTravelerCount;

    @Column(name = "payment_sold_product_option_price", nullable = false)
    private int paymentSoldProductOptionPrice;

    @Builder
    public OrderDetail(com.example.travel.domain.Order order, ProductOption productOption, int paymentDetailTravelerCount, int paymentSoldProductOptionPrice) {
        Order = order;
        this.productOption = productOption;
        this.paymentDetailTravelerCount = paymentDetailTravelerCount;
        this.paymentSoldProductOptionPrice = paymentSoldProductOptionPrice;
    }
}
