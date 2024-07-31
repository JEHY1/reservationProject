package com.example.travel.service;

import com.example.travel.repository.OrderDetailRepository;
import com.example.travel.repository.OrderRepository;
import com.example.travel.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

}
