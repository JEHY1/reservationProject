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
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    @Column(name = "order_detail_traveler_count", nullable = false)
    private int orderDetailTravelerCount;

    @Column(name = "order_detail_total_sold_product_option_regular_price", nullable = false)
    private int orderDetailTotalSoldProductOptionRegularPrice;

    @Column(name ="order_detail_total_sold_product_option_discount_price", nullable = false)
    private int orderDetailTotalSoldProductOptionDiscountPrice;

    @Builder
    public OrderDetail(com.example.travel.domain.Order order, ProductOption productOption, int orderDetailTravelerCount, int orderDetailTotalSoldProductOptionRegularPrice, int orderDetailTotalSoldProductOptionDiscountPrice) {
        this.order = order;
        this.productOption = productOption;
        this.orderDetailTravelerCount = orderDetailTravelerCount;
        this.orderDetailTotalSoldProductOptionRegularPrice = orderDetailTotalSoldProductOptionRegularPrice;
        this.orderDetailTotalSoldProductOptionDiscountPrice = orderDetailTotalSoldProductOptionDiscountPrice;
    }
}
