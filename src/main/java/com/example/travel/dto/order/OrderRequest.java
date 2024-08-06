package com.example.travel.dto.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderRequest {

    private long productId;
    private String selectedDate;
    private List<Long> optionId;
    private List<Integer> count;
}
