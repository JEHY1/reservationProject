package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", updatable = false)
    private long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "order_departure_date", nullable = false)
    private LocalDateTime orderDepartureDate;

    @Column(name = "order_end_date", nullable = false)
    private LocalDateTime orderEndDate;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "order_payment_type", nullable = false)
    private String orderPaymentType;

    @Column(name = "order_total_price", nullable = false)
    private int orderTotalPrice;

    @Column(name = "order_account")
    private String orderAccount;

    @Column(name = "order_depositor")
    private String orderDepositor;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private List<OrderDetail> orderDetailList;

    @ToString.Exclude
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Review review;

    @ToString.Exclude
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Payment payment;

    @Builder
    public Order(Product product, User user, LocalDateTime orderDepartureDate, LocalDateTime orderEndDate, String orderStatus, String orderPaymentType, int orderTotalPrice, String orderAccount, String orderDepositor) {
        this.product = product;
        this.user = user;
        this.orderDepartureDate = orderDepartureDate;
        this.orderEndDate = orderEndDate;
        this.orderStatus = orderStatus;
        this.orderPaymentType = orderPaymentType;
        this.orderTotalPrice = orderTotalPrice;
        this.orderAccount = orderAccount;
        this.orderDepositor = orderDepositor;
    }
}
