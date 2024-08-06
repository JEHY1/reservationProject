package com.example.travel.dto.order;

import com.example.travel.domain.ProductOption;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OptionCountForm {

    private ProductOption productOption;
    private int count;

    @Builder
    public OptionCountForm(ProductOption productOption, int count) {
        this.productOption = productOption;
        this.count = count;
    }
}
