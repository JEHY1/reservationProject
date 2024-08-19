package com.example.travel.controller.api;

import com.example.travel.dto.product.ProductNameForm;
import com.example.travel.dto.product.SearchRequest;
import com.example.travel.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchApiController {

    private final ProductService productService;

    @PostMapping("/api/searchProduct")
    public ResponseEntity<List<ProductNameForm>> searchProductNameIn(@RequestBody SearchRequest request){
        System.err.println("call search");
        return ResponseEntity.ok()
                .body(productService.searchProductNameContaining(request));
    }

}
