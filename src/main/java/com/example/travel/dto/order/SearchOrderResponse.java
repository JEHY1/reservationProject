package com.example.travel.dto.order;

import com.example.travel.domain.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SearchOrderResponse {

    List<Order> orderList;
    int totalPages;
    int totalElements;

    @Builder
    public SearchOrderResponse(List<Order> orderList, int totalPages, int totalElements) {
        this.orderList = orderList;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
