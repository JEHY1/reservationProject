package com.example.travel.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ProductNameForm {
    private String frontText;
    private String searchText;
    private String endText;

    @Builder
    public ProductNameForm(String frontText, String searchText, String endText) {
        this.frontText = frontText;
        this.searchText = searchText;
        this.endText = endText;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof ProductNameForm){
            ProductNameForm castingObj = (ProductNameForm) obj;
            if(castingObj.frontText.equals(this.frontText) && castingObj.searchText.equals(this.searchText) && castingObj.endText.equals(this.endText)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.frontText, this.searchText, this.endText);
    }
}
