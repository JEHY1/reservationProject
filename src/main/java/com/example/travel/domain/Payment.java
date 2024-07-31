package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", updatable = false)
    private long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "payment_price", nullable = false)
    private int paymentPrice;

    @CreatedDate
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "payment_depositor")
    private String paymentDepositor;

    @Column(name = "payment_card_company")
    private String paymentCardCompany;

    @Column(name = "payment_card_number")
    private String paymentCardNumber;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    public Payment(Order order, int paymentPrice, String paymentType, String paymentDepositor, String paymentCardCompany, String paymentCardNumber, String paymentStatus) {

        this.order = order;
        this.paymentPrice = paymentPrice;
        this.paymentType = paymentType;
        this.paymentDepositor = paymentDepositor;
        this.paymentCardCompany = paymentCardCompany;
        this.paymentCardNumber = paymentCardNumber;
        this.paymentStatus = paymentStatus;
    }
}