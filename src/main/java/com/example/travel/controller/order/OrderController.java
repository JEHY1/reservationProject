package com.example.travel.controller.order;

import com.example.travel.domain.Product;
import com.example.travel.dto.order.OrderRequest;
import com.example.travel.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final ProductService productService;

    @PostMapping("/order")
    public String orderPage(OrderRequest request, Principal principal){
        System.err.println(request);
        System.err.println(productService.orderResponse(request, principal));
        return "/order/order";
    }
}
