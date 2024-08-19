package com.example.travel.controller.seller;

import com.example.travel.dto.order.SearchOrderRequest;
import com.example.travel.service.PaymentService;
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
public class SellerController {

    private final PaymentService paymentService;
    private final ProductService productService;
    private final UserService userService;

    @GetMapping("/seller/orderList")
    public String orderListPage(SearchOrderRequest request, @PageableDefault(page = 0, size = 10) Pageable pageable, Principal principal, Model model){

        model.addAttribute("productList", productService.findProductByPrincipal(principal));
        model.addAttribute("orderInfo", paymentService.searchOrder(request, principal, pageable));
        model.addAttribute("path", "seller");

        return "/seller/orderManager";
    }

    @GetMapping("/seller/orderDetail/{orderId}")
    public String orderDetailPage(@PathVariable long orderId, Principal principal, Model model){

        System.err.println(paymentService.findOrderByOrderId(orderId));
        System.err.println(paymentService.isReservationUpdatable(orderId));
        paymentService.orderCheck(orderId);
        model.addAttribute("order", paymentService.findOrderByOrderId(orderId));
        model.addAttribute("isOrderUpdatable", paymentService.isReservationUpdatable(orderId));
        model.addAttribute("path", "seller");

        return "/seller/orderDetailManager";
    }

    @GetMapping("/admin/orderList")
    public String orderListPageAdmin(SearchOrderRequest request, @PageableDefault(page = 0, size = 10) Pageable pageable, Principal principal, Model model){

        model.addAttribute("productList", productService.findAllProduct());
        model.addAttribute("orderInfo", paymentService.searchOrder(request, principal, pageable));
        model.addAttribute("path", "admin");

        return "/seller/orderManager";
    }

    @GetMapping("/admin/orderDetail/{orderId}")
    public String orderDetailPageAdmin(@PathVariable long orderId, Principal principal, Model model){


        model.addAttribute("order", paymentService.findOrderByOrderId(orderId));
        model.addAttribute("isOrderUpdatable", paymentService.isReservationUpdatable(orderId));
        System.err.println(paymentService.findOrderByOrderId(orderId));
        System.err.println(paymentService.isReservationUpdatable(orderId));
        model.addAttribute("path", "admin");

        return "/seller/orderDetailManager";
    }
}
