package com.example.travel.service;

import com.example.travel.domain.Order;
import com.example.travel.domain.OrderDetail;
import com.example.travel.domain.Payment;
import com.example.travel.domain.Product;
import com.example.travel.dto.order.OrderSubmitRequest;
import com.example.travel.dto.order.SearchOrderRequest;
import com.example.travel.dto.order.SearchOrderResponse;
import com.example.travel.dto.payment.PaymentSubmitRequest;
import com.example.travel.repository.OrderDetailRepository;
import com.example.travel.repository.OrderRepository;
import com.example.travel.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;
    private final UserService userService;

    public Order order(OrderSubmitRequest request, Principal principal){
        System.err.println("orderDepartDate : " + request.getOrderDepartDate());
        String[] date = request.getOrderDepartDate().split("-");
        LocalDateTime departDate = LocalDateTime.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 0, 0, 0);
        LocalDateTime endDate = departDate.plusDays(4).minusSeconds(1);

        Order order = orderRepository.save(Order.builder()
                .product(productService.findProductByProductId(request.getProductId()))
                .user(userService.getUserByPrincipal(principal))
                .orderDepartureDate(departDate)
                .orderEndDate(endDate)
                .orderStatus("미입금")
                .orderPaymentType(request.getPaymentType())
                .orderTotalPrice(request.getTotalPrice())
                .orderAccount(request.getAccount().isEmpty() ? null : request.getAccount())
                .orderDepositor(request.getDepositor().isEmpty() ? null : request.getDepositor())
                .build());

        int index = 0;
        for(long optionId : request.getOptionList()){
            orderDetailRepository.save(OrderDetail.builder()
                    .order(order)
                    .productOption(productService.findProductOptionByProductOptionId(optionId))
                    .orderDetailTravelerCount(request.getCountList().get(index))
                    .orderDetailTotalSoldProductOptionRegularPrice(request.getTotalOptionRegularPriceList().get(index))
                    .orderDetailTotalSoldProductOptionDiscountPrice(request.getTotalOptionDiscountPriceList().get(index++))
                    .build());
        }
        return order;
    }

    public Page<Order> findOrderByPrincipalWithPage(Principal principal, Pageable pageable){
        return orderRepository.findAllByUserUserId(userService.getUserId(principal), pageable)
                .orElseThrow(() -> new IllegalArgumentException("not found Order"));
    }

    public Order findOrderByOrderId(long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("not found order"));
    }

    public Payment payment(PaymentSubmitRequest request){
        Order order = findOrderByOrderId(request.getOrderId());
        String[] account = request.getPaymentRefundAccount().split(" ");

        return paymentRepository.save(Payment.builder()
                .order(order)
                .paymentPrice(order.getOrderTotalPrice())
                .paymentType(order.getOrderPaymentType())
                .paymentDepositor(order.getOrderDepositor())
                .paymentCardCompany(account[0])
                .paymentCardNumber(account[1])
                .paymentStatus("결제확인대기")
                .paymentRefundAccount(request.getPaymentRefundAccount())
                .paymentRefundAccountName(request.getPaymentRefundAccountName())
                .build());
    }

    public SearchOrderResponse searchOrder(SearchOrderRequest request, Principal principal, Pageable pageable){

        List<Product> productList = productService.findProductByPrincipal(principal);
        List<Order> orderList = findOrderByProductIdIn(productList.stream().mapToLong(Product::getProductId).boxed().collect(Collectors.toList()));

        //상품 id로 필터
        if(request.getProductId() != null){
            orderList = orderList.stream().filter(order -> order.getProduct().getProductId() == request.getProductId()).collect(Collectors.toList());
        }

        //주문 일자(시작일)로 필터
        if(request.getOrderDate1() != null){
            orderList = orderList.stream().filter(order -> order.getOrderDate().isAfter(request.getOrderDate1().atStartOfDay()) || order.getOrderDate().isEqual(request.getOrderDate1().atStartOfDay())).collect(Collectors.toList());
        }

        //주문 일자(종료일)로 필터
        if(request.getOrderDate2() != null){
            orderList = orderList.stream().filter(order -> order.getOrderDate().isBefore(request.getOrderDate2().atStartOfDay().plusDays(1).minusSeconds(1)) || order.getOrderDate().isEqual(request.getOrderDate2().atStartOfDay().plusDays(1).minusSeconds(1))).collect(Collectors.toList());
        }

        //예약일(시작일)로 필터
        if(request.getReservationDate1() != null){
            orderList = orderList.stream().filter(order -> order.getOrderDepartureDate().isAfter(request.getReservationDate1().atStartOfDay()) || order.getOrderDepartureDate().isEqual(request.getReservationDate1().atStartOfDay())).collect(Collectors.toList());
        }

        //예약일(종료일)로 필터
        if(request.getReservationDate2() != null){
            orderList = orderList.stream().filter(order -> order.getOrderDepartureDate().isBefore(request.getReservationDate2().atStartOfDay().plusDays(1).minusSeconds(1))).collect(Collectors.toList());
        }

        //결제방식으로 필터
        if(request.getPaymentType() != null){
            orderList = orderList.stream().filter(order -> order.getOrderPaymentType().equals(request.getPaymentType())).collect(Collectors.toList());
        }

        //입금자명으로 필터
        if(request.getDepositorName() != null){
            orderList = orderList.stream().filter(order -> order.getOrderDepositor() != null && order.getOrderDepositor().contains(request.getDepositorName())).collect(Collectors.toList());
        }

        //금액(최소값)으로 필터
        if(request.getPaymentPrice1() != null){
            orderList = orderList.stream().filter(order -> order.getOrderTotalPrice() >= request.getPaymentPrice1()).collect(Collectors.toList());
        }

        //금액(최대값)으로 필터
        if(request.getPaymentPrice2() != null){
            orderList = orderList.stream().filter(order -> order.getOrderTotalPrice() <= request.getPaymentPrice2()).collect(Collectors.toList());
        }

        //주문상태로 필터
        if(request.getOrderStatus() != null){
            orderList = orderList.stream().filter(order -> order.getOrderStatus().equals(request.getOrderStatus())).collect(Collectors.toList());
        }

        return SearchOrderResponse.builder()
                .orderList(orderList.subList(pageable.getPageNumber() * 10, Math.min(orderList.size(), (pageable.getPageNumber() + 1) * 10)))
                .totalElements(orderList.size())
                .totalPages(orderList.size() / 10 + 1)
                .build();
    }

    public List<Order> findOrderByProductIdIn(List<Long> productIdList){
        return orderRepository.findAllByProductProductIdIn(productIdList)
                .orElseThrow(() -> new IllegalArgumentException("not found Order"));
    }
}
