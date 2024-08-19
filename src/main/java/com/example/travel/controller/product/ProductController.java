package com.example.travel.controller.product;

import com.example.travel.service.ProductService;
import com.example.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/product/{productId}")
    public String productDetailPage(@PathVariable(required = false)Long productId, Model model, Principal principal){


        model.addAttribute("userId", principal != null ? userService.getUserId(principal) : null);
        model.addAttribute("productInfo", productService.getProductDetailPageInfo(productId));
        System.err.println(productService.getProductDetailPageInfo(productId));
        return "/product/productDetail";
    }

    @GetMapping("/product")
    public String productGroup(@RequestParam(required = false) String searchText, @RequestParam(required = false) String mainCategory, @RequestParam(required = false) String subCategory, @PageableDefault(page = 0, size = 10, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable, Model model){
        model.addAttribute("productGroupPage", productService.searchProduct(mainCategory, subCategory, searchText, pageable));
        return "/product/productGroup";
    }
}
