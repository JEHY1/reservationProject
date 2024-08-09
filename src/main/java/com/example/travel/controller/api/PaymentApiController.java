package com.example.travel.controller.api;

import com.example.travel.domain.Order;
import com.example.travel.domain.Payment;
import com.example.travel.dto.order.OrderRequest;
import com.example.travel.dto.order.OrderSubmitRequest;
import com.example.travel.dto.payment.PaymentSubmitRequest;
import com.example.travel.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping("/api/order")
    public ResponseEntity<Order> order(@RequestBody OrderSubmitRequest request, Principal principal){
        System.err.println(request);

        return ResponseEntity.ok()
                .body(paymentService.order(request, principal));
    }

    @PostMapping("/api/payment/deposit")
    public ResponseEntity<Payment> payment(@RequestBody PaymentSubmitRequest request){
        return ResponseEntity.ok()
                .body(paymentService.payment(request));
    }

}
