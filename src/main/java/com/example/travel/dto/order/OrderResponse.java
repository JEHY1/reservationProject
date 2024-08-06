package com.example.travel.dto.order;

import com.example.travel.domain.Account;
import com.example.travel.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderResponse {

    private String productRepImg;
    private List<OptionCountForm> optionList;
    private int totalRegularPrice;
    private int totalDiscount;
    private List<Account> accountList;
    private User user;
    private String departDate;

    @Builder
    public OrderResponse(String productRepImg, List<OptionCountForm> optionList, int totalRegularPrice, int totalDiscount, List<Account> accountList, User user, String departDate) {
        this.productRepImg = productRepImg;
        this.optionList = optionList;
        this.totalRegularPrice = totalRegularPrice;
        this.totalDiscount = totalDiscount;
        this.accountList = accountList;
        this.user = user;
        this.departDate = departDate;
    }
}
