package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private List<OrderDetail> orderDetailList;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Review review;

    @Builder
    public Order(Product product, User user, LocalDateTime orderDepartureDate, LocalDateTime orderEndDate, String orderStatus) {
        this.product = product;
        this.user = user;
        this.orderDepartureDate = orderDepartureDate;
        this.orderEndDate = orderEndDate;
        this.orderStatus = orderStatus;
    }
}
