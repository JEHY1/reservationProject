package com.example.travel.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "account_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", updatable = false)
    private long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "account_bank")
    private String accountBank;

    @Column(name = "account_number")
    private String accountNumber;

    @Builder
    public Account(User user, String accountBank, String accountNumber) {
        this.user = user;
        this.accountBank = accountBank;
        this.accountNumber = accountNumber;
    }
}
