package com.example.travel.controller.product;

import com.example.travel.service.ProductService;
import com.example.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/product/{productId}")
    public String productDetailPage(@PathVariable(required = false)Long productId, Model model){

        model.addAttribute("productInfo", productService.getProductDetailPageInfo(productId));
        System.err.println(productService.getProductDetailPageInfo(productId));
        return "/product/productDetail";
    }
}
