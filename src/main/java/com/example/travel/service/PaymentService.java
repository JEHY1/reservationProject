package com.example.travel.service;

import com.example.travel.domain.*;
import com.example.travel.dto.admin.ScheduleDateDto;
import com.example.travel.dto.order.*;
import com.example.travel.dto.payment.PaymentSubmitRequest;
import com.example.travel.dto.review.MyWritableReview;
import com.example.travel.dto.review.MyWritableReviewResponse;
import com.example.travel.dto.review.MyWrittenReview;
import com.example.travel.dto.review.MyWrittenReviewResponse;
import com.example.travel.dto.user.OrderHistoryResponse;
import com.example.travel.repository.OrderDetailRepository;
import com.example.travel.repository.OrderRepository;
import com.example.travel.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Temporals;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        String[] date = request.getOrderDepartDate().split("-");
        LocalDateTime departDate = LocalDateTime.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 0, 0, 0);
        LocalDateTime endDate = departDate.plusDays(productService.findProductByProductId(request.getProductId()).getProductTravelDays()).minusSeconds(1);

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
                    .orderDetailTotalSoldProductOptionDiscountPrice(request.getTotalOptionDiscountPriceList().get(index) != null ? request.getTotalOptionDiscountPriceList().get(index++) : request.getTotalOptionRegularPriceList().get(index++))
                    .build());
        }
        return order;
    }

    public Page<Order> findOrderByPrincipalWithPage(Principal principal, Pageable pageable){
        Page<Order> orderList = orderRepository.findAllByUserUserId(userService.getUserId(principal), pageable)
                .orElseThrow(() -> new IllegalArgumentException("not found Order"));

        orderList.getContent().forEach(order -> {
            if(!order.getOrderDetailList().isEmpty()){
                order.getOrderDetailList().forEach(orderDetail -> {
                    order.setTotalCount(order.getTotalCount() + orderDetail.getOrderDetailTravelerCount());
                });
            }
        });

        return orderList;
    }

    public Order findOrderByOrderId(long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("not found order"));
    }

    @Transactional
    public Payment payment(PaymentSubmitRequest request){
        Order order = findOrderByOrderId(request.getOrderId());
        String[] account = request.getPaymentRefundAccount().split(" ");

        order.updateStatus("입금미확인");

        Payment payment = findPaymentByOrderId(request.getOrderId());

        if(payment == null){
            paymentRepository.save(Payment.builder()
                    .order(order)
                    .paymentPrice(order.getOrderTotalPrice())
                    .paymentType(order.getOrderPaymentType())
                    .paymentDepositor(order.getOrderDepositor())
                    .paymentCardCompany(account[0])
                    .paymentCardNumber(account[1])
                    .paymentStatus("입금미확인")
                    .paymentRefundAccount(request.getPaymentRefundAccount())
                    .paymentRefundAccountName(request.getPaymentRefundAccountName())
                    .paymentCheck("확인요청")
                    .build());

            return Payment.builder()
                    .paymentCheck("신규 결제 성공")
                    .build();
        }
        else{
            if(payment.getPaymentCheck() == null){
                payment.updatePaymentCheck("확인재요청");
                payment.updateAccountInfo(request.getPaymentRefundAccount());
                return Payment.builder()
                        .paymentCheck("재요청 성공")
                        .build();
            }

            return Payment.builder()
                    .paymentCheck("유효한 요청이 있음")
                    .build();
        }
    }

    public SearchOrderResponse searchOrder(SearchOrderRequest request, Principal principal, Pageable pageable){

        List<Order> orderList;
        if(userService.getUserByPrincipal(principal).getUserRole().equals("ADMIN")){
            orderList = findAllOrder();
        }
        else{
            List<Product> productList = productService.findProductByPrincipal(principal);
            orderList = findOrderByProductIdInSortByOrderDate(productList.stream().mapToLong(Product::getProductId).boxed().collect(Collectors.toList()));
        }

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

        List<Order> subList = orderList.subList(pageable.getPageNumber() * 10, Math.min(orderList.size(), (pageable.getPageNumber() + 1) * 10));
        if(!subList.isEmpty()){
            subList.forEach(order -> {
                if(!order.getOrderDetailList().isEmpty()){
                    order.getOrderDetailList().forEach(orderDetail -> {
                        order.setTotalCount(order.getTotalCount() + orderDetail.getOrderDetailTravelerCount());
                    });
                }
            });
        }

        return SearchOrderResponse.builder()
                .orderList(orderList.subList(pageable.getPageNumber() * 10, Math.min(orderList.size(), (pageable.getPageNumber() + 1) * 10)))
                .totalElements(orderList.size())
                .totalPages((orderList.size() - 1) / 10 + 1)
                .build();
    }

    public List<Order> findOrderByProductIdInSortByOrderDate(List<Long> productIdList){
        return orderRepository.findAllByProductProductIdInOrderByOrderDateDesc(productIdList)
                .orElseThrow(() -> new IllegalArgumentException("not found Order"));
    }

    public Payment findPaymentByOrderId(long orderId){
        return paymentRepository.findByOrderOrderId(orderId)
                .orElse(null);
    }

    @Transactional
    public void orderCancel(OrderControlRequest request){
        Order order = findOrderByOrderId(request.getOrderId());
        Payment payment = order.getPayment();
        String updateStatus = order.getOrderStatus().equals("결제완료") ? "환불진행" : "주문취소";

        order.updateStatus(updateStatus);

        if(payment != null){
            payment.updatePaymentCheck(null);
            payment.updatePaymentStatus(updateStatus);
            order.updateStatus(updateStatus);
        }
    }

    public boolean isReservationUpdatable(long orderId){
        return findOrderByOrderId(orderId).getOrderDepartureDate().isAfter(LocalDate.now().atStartOfDay());
    }

    @Transactional
    public Order updateOrderAndPaymentStatus(OrderControlRequest request, String status){
        Order order = findOrderByOrderId(request.getOrderId());
        Payment payment = order.getPayment();
        order.updateStatus(status);
        if(payment != null){
            payment.updatePaymentStatus(status);
        }
        return order;
    }

    @Transactional
    public void orderCheck(long orderId){
        Payment payment = findOrderByOrderId(orderId).getPayment();
        if(payment != null && payment.getPaymentCheck() != null){
            payment.updatePaymentCheck(null);
        }
    }

    public MyWritableReviewResponse myWritableReviewList(Principal principal, Pageable pageable){
        List<Order> writableReviewAndWrittenReviewList = orderRepository.findAllByUserUserIdAndOrderEndDateIsBeforeAndOrderEndDateIsAfterAndOrderStatusOrderByOrderDepartureDate(userService.getUserId(principal), LocalDateTime.now(), LocalDate.now().atStartOfDay().minusDays(31), "결제완료").orElse(null);


        if(writableReviewAndWrittenReviewList != null){
            writableReviewAndWrittenReviewList = writableReviewAndWrittenReviewList.stream().filter(order -> order.getReview() == null).collect(Collectors.toList());

            if(!writableReviewAndWrittenReviewList.isEmpty()){

                return MyWritableReviewResponse.builder()
                        .myWritableReviewList(writableReviewAndWrittenReviewList.stream().map(this::toMyWritableReview).collect(Collectors.toList()).subList(pageable.getPageNumber() * 10, Math.min(writableReviewAndWrittenReviewList.size(), (pageable.getPageNumber() + 1) * 10)))
                        .totalElements(writableReviewAndWrittenReviewList.size())
                        .totalPages((writableReviewAndWrittenReviewList.size() - 1) / 10 + 1)
                        .build();
            }
        }
        return MyWritableReviewResponse.builder()
                .totalPages(0)
                .totalElements(0)
                .build();
    }

    public MyWritableReview toMyWritableReview(Order order){
        return MyWritableReview.builder()
                .orderId(order.getOrderId())
                .productId(order.getProduct().getProductId())
                .orderDepartureDate(order.getOrderDepartureDate())
                .orderEndDate(order.getOrderEndDate())
                .productName(order.getProduct().getProductTitle())
                .productRepImg(order.getProduct().getProductRepImgList().get(0).getProductRepImgSrc())
                .productId(order.getProduct().getProductId())
                .deadLine(order.getOrderEndDate().plusDays(31))
                .build();
    }

    public MyWrittenReviewResponse myWrittenReviewList(Principal principal, Pageable pageable){
        List<Order> writableReviewAndWrittenReviewList = orderRepository.findAllByUserUserIdAndOrderEndDateIsBeforeAndOrderStatusOrderByOrderDepartureDateDesc(userService.getUserId(principal), LocalDateTime.now(), "결제완료").orElse(null);

        if(writableReviewAndWrittenReviewList != null){
            writableReviewAndWrittenReviewList = writableReviewAndWrittenReviewList.stream().filter(order -> order.getReview() != null).collect(Collectors.toList());
            return MyWrittenReviewResponse.builder()
                    .myWrittenReviewList(writableReviewAndWrittenReviewList.stream().map(this::toMyWrittenReview).collect(Collectors.toList()).subList(pageable.getPageNumber() * 10, Math.min(writableReviewAndWrittenReviewList.size(), (pageable.getPageNumber() + 1) * 10)))
                    .totalElements(writableReviewAndWrittenReviewList.size())
                    .totalPages((writableReviewAndWrittenReviewList.size() + 1) / 10 + 1)
                    .build();
        }
        return MyWrittenReviewResponse.builder()
                .totalPages(0)
                .totalElements(0)
                .build();
    }

    public MyWrittenReview toMyWrittenReview(Order order){
        return MyWrittenReview.builder()
                .review(order.getReview())
                .productName(order.getProduct().getProductTitle())
                .reviewRepImg(order.getReview().getReviewImgList().isEmpty() ? null : order.getReview().getReviewImgList().get(0).getReviewImgSrc())
                .productRepImg(order.getProduct().getProductRepImgList().get(0).getProductRepImgSrc())
                .isReviewUpdatable(LocalDateTime.now().isBefore(order.getReview().getReviewSubmitDate().toLocalDate().atStartOfDay().plusDays(91).minusSeconds(1)))
                .build();
    }

    public OrderHistoryResponse myOrderHistory(Principal principal){
        int nonDeposit = 0;
        int depositNonCheck = 0;
        int paymentComplete = 0;
        int orderCancel = 0;
        int refunding = 0;
        int refundComplete = 0;

        List<Order> orderList = findOrderByPrincipal(principal);

        for(Order order : orderList){
            if(order.getOrderStatus().equals("미입금")){
                nonDeposit++;
            }
            else if(order.getOrderStatus().equals("입금미확인")){
                depositNonCheck++;
            }
            else if(order.getOrderStatus().equals("결제완료")){
                paymentComplete++;
            }
            else if(order.getOrderStatus().equals("주문취소")){
                orderCancel++;
            }
            else if(order.getOrderStatus().equals("환불진행")){
                refunding++;
            }
            else if(order.getOrderStatus().equals("환불완료")){
                refundComplete++;
            }
        }

        return OrderHistoryResponse.builder()
                .nonDeposit(nonDeposit)
                .depositNonCheck(depositNonCheck)
                .paymentComplete(paymentComplete)
                .orderCancel(orderCancel)
                .refunding(refunding)
                .refundComplete(refundComplete)
                .build();
    }

    public List<Order> findOrderByPrincipal(Principal principal){
        return orderRepository.findAllByUserUserId(userService.getUserId(principal))
                .orElseThrow(() -> new IllegalArgumentException("not found Order"));
    }

    public List<Order> findAllOrder(){
        return orderRepository.findAll();

    }

    //특정 기간 매출 합계(상품 전체, 혹은 단일 상품)
    public List<Long> paymentHistory(Principal principal, Long productId){
        LocalDate now = LocalDate.now();
        List<String> orderStatusList = new ArrayList<>();
        orderStatusList.add("결제완료");
        orderStatusList.add("입금미확인");
        orderStatusList.add("미입금");
        LocalDateTime startPoint = LocalDate.of(now.getYear(), now.getMonth(), 1).minusMonths(5).atStartOfDay();
        List<Long> productIdList = productService.findProductByPrincipal(principal).stream().mapToLong(Product::getProductId).boxed().collect(Collectors.toList());

        if(productId != null){
            productIdList.clear();
            productIdList.add(productId);
        }
        List<Order> orderList = findOrderByProductIdInAndOrderDateAfterAndOrderStatusIn(productIdList, startPoint, orderStatusList);

        List<Long> totalPaymentHistory = new ArrayList<>();
        long totalPaymentMonth1 = 0;
        long totalPaymentMonth2 = 0;
        long totalPaymentMonth3 = 0;

        for(Order order : orderList){
            if(order.getOrderDate().isAfter(startPoint.plusMonths(5))){
                if((order.getOrderStatus().equals("미입금") || order.getOrderStatus().equals("입금미확인")) && LocalDateTime.now().isAfter(order.getOrderDepartureDate())){
                    continue;
                }
                totalPaymentMonth1 += order.getOrderTotalPrice();
            }
            else if(order.getOrderDate().isAfter(startPoint.plusMonths(4))){
                if((order.getOrderStatus().equals("미입금") || order.getOrderStatus().equals("입금미확인")) && LocalDateTime.now().isAfter(order.getOrderDepartureDate())){
                    continue;
                }
                totalPaymentMonth2 += order.getOrderTotalPrice();
            }
            else if(order.getOrderDate().isAfter(startPoint.plusMonths(3))){
                if((order.getOrderStatus().equals("미입금") || order.getOrderStatus().equals("입금미확인")) && LocalDateTime.now().isAfter(order.getOrderDepartureDate())){
                    continue;
                }
                totalPaymentMonth3 += order.getOrderTotalPrice();
            }
        }

        return Arrays.stream(new Long[]{totalPaymentMonth1, totalPaymentMonth2, totalPaymentMonth3}).collect(Collectors.toList());
    }

    public List<Order> findOrderByProductIdInAndOrderDateAfterAndOrderStatusIn(List<Long> productIdList, LocalDateTime orderDate, List<String> orderStatusList){
        return orderRepository.findAllByProductProductIdInAndOrderDateIsAfterAndOrderStatusIn(productIdList, orderDate, orderStatusList)
                .orElseThrow(() -> new IllegalArgumentException("not found order"));
    }

    public List<OptionSellDto> getOptionSellCount(long productId){
        LocalDate now = LocalDate.now();
        LocalDateTime startDate = LocalDate.of(now.getYear(), now.getMonth(), 1).atStartOfDay(); //검색 기간 시작일
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1); //검색기간 종료일
        List<OptionSellDto>OptionSellStoList = orderRepository.getOptionSellCount(productId, startDate, endDate, LocalDateTime.now());

        return OptionSellStoList;
    }

    public long getTotalOptionCount(List<OptionSellDto> list){
        int sum = 0;
        for(OptionSellDto dto : list){
            sum += dto.totalSellCount;
        }
        return sum;
    }

    public List<Long> getOrderStatusStatistics(long productId){
        LocalDate now = LocalDate.now();
        LocalDateTime startDate = LocalDate.of(now.getYear(), now.getMonth(), 1).atStartOfDay(); //검색 기간 시작일
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1); //검색기간 종료일
        List<OrderStatusAndCountDto> orderStatusAndCountDtoList = orderRepository.getOrderStatusStatistics(productId, startDate, endDate);
        System.err.println(orderStatusAndCountDtoList);
        List<Long> returnList = new ArrayList<>();
        Long[] longArray = new Long[]{0L, 0L, 0L, 0L, 0L, 0L, 0L};

        orderStatusAndCountDtoList.forEach(value -> {
            if(value.getOrderStatus().equals("결제완료")){
                longArray[0] = value.getOrderStatusCount();
            }
            else if (value.getOrderStatus().equals("입금미확인")) {
                longArray[1] = value.getOrderStatusCount();
            }
            else if (value.getOrderStatus().equals("미입금")) {
                longArray[2] = value.getOrderStatusCount();
            }
            else if (value.getOrderStatus().equals("주문취소")) {
                longArray[3] = value.getOrderStatusCount();
            }
            else if (value.getOrderStatus().equals("환불완료")) {
                longArray[4] = value.getOrderStatusCount();
            }
            else if (value.getOrderStatus().equals("환불진행")) {
                longArray[5] = value.getOrderStatusCount();
            }
            longArray[6] += value.getOrderStatusCount();
        });

        return Arrays.stream(longArray).collect(Collectors.toList());
    }

    public OrderDateStatisticsResponse getOrderDateStatistics(long productId, int year, int month){

        LocalDateTime startDate = LocalDate.of(year, month, 1).atStartOfDay(); //검색 기간 시작일
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1); //검색기간 종료일
        List<OrderDateStatistics> orderDateStatisticsList = orderRepository.getOrderDateStatistics(productId, startDate, endDate, LocalDateTime.now());

        System.err.println(orderDateStatisticsList);
        List<String> dateList = orderDateStatisticsList.stream().map(dto -> dto.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))).collect(Collectors.toList());
        List<Long> departureCountList = orderDateStatisticsList.stream().mapToLong(OrderDateStatistics::getDepartureCount).boxed().toList();

        return OrderDateStatisticsResponse.builder()
                .orderDateList(dateList)
                .orderDepartureCountList(departureCountList)
                .build();
    }

    public ScheduleDateDto getScheduleDate(int year, int month, long productId){
        Product product = productService.findProductByProductId(productId);

        return ScheduleDateDto.builder()
                .year(year)
                .month(month)
                .productStartDate(product.getProductStartDate())
                .productEndDate(product.getProductEndDate())
                .maxDepartureCount(product.getProductMaxCount())
                .build();

    }
}
