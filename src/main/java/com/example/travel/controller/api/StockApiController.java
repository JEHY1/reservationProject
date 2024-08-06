package com.example.travel.controller.api;

import com.example.travel.dto.product.StockRequest;
import com.example.travel.dto.product.StockResponse;
import com.example.travel.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class StockApiController {

    private final ProductService productService;

    @PostMapping("/api/getStock")
    public ResponseEntity<StockResponse> responseStock(@RequestBody StockRequest request){
        System.err.println("call api");
        return ResponseEntity.ok()
                .body(productService.getStock(request));
    }

}
