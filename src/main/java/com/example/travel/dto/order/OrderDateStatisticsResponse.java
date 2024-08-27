package com.example.travel.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDateStatisticsResponse {
    private List<String> orderDateList;
    private List<Long> orderDepartureCountList;

    @Builder
    public OrderDateStatisticsResponse(List<String> orderDateList, List<Long> orderDepartureCountList) {
        this.orderDateList = orderDateList;
        this.orderDepartureCountList = orderDepartureCountList;
    }
}
