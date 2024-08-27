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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/seller/totalManager")
    public String totalManagerPage(@RequestParam(required = false) Long productId, @RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month, Model model, Principal principal){

        //지난 3개월 판매금액(1개월당)
        model.addAttribute("paymentHistoryList", paymentService.paymentHistory(principal, productId));
        model.addAttribute("date", LocalDateTime.now());
        
        //상품 목록
        model.addAttribute("productList", productService.findProductByPrincipal(principal));

        if(productId == null){
            //판매량 순위 및 저번달 기준 변동폭
            model.addAttribute("productRankList", productService.getProductRank());
        }
        else{
            model.addAttribute("product", productService.findProductByProductId(productId));

            year = year != null ? year : LocalDate.now().getYear();
            month = month != null ? month : LocalDate.now().getMonthValue();
            //이번달 해당 상품 옵션 총 선택수
            model.addAttribute("optionStatisticsList", paymentService.getOptionSellCount(productId));
            model.addAttribute("totalOptionCount", paymentService.getTotalOptionCount(paymentService.getOptionSellCount(productId)));

            //이번달 해당 상품 판매상태 통계
            model.addAttribute("orderStatusStatisticsList", paymentService.getOrderStatusStatistics(productId));

            //일별 예약수량 통계
            model.addAttribute("calendarInfo", paymentService.getOrderDateStatistics(productId, year, month));

            //스케줄 날짜(년, 월), 최대수량
            model.addAttribute("scheduleDate", paymentService.getScheduleDate(year, month, productId));
        }

        return "/seller/schedule";
    }
}
