package com.example.travel.controller.order;

import com.example.travel.dto.order.OrderRequest;
import com.example.travel.service.PaymentService;
import com.example.travel.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final ProductService productService;
    private final PaymentService paymentService;
    @PostMapping("/order")
    public String orderPage(OrderRequest request, Principal principal, Model model){
        System.err.println(request);
        System.err.println(productService.orderResponse(request, principal));
        model.addAttribute("orderInfo", productService.orderResponse(request, principal));
        return "/order/order";
    }

    @GetMapping("/orderList")
    public String orderListPage(Principal principal, @PageableDefault(page = 0, size = 10, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable, Model model){
        System.err.println(paymentService.findOrderByPrincipalWithPage(principal, pageable).getTotalPages());

        model.addAttribute("orderList", paymentService.findOrderByPrincipalWithPage(principal, pageable));
        return "/order/orderList";
    }

    @GetMapping("/orderDetail/{orderId}")
    public String orderDetailPage(Principal principal, Model model, @PathVariable long orderId){

        model.addAttribute("order", paymentService.findOrderByOrderId(orderId));


        return "/order/orderDetail";
    }
}
