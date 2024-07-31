package com.example.travel.controller.test;

import com.example.travel.domain.Account;
import com.example.travel.domain.Payment;
import com.example.travel.domain.User;
import com.example.travel.repository.*;
import com.example.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class testController {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductInfoImgRepository productInfoImgRepository;
    private final ProductRepImgRepository productRepImgRepository;
    private final ProductRepository productRepository;
    private final QnaRepository qnaRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;



    @GetMapping("/test")
    public String test(Model model){
        model.addAttribute("accounts", userService.findAllAccount());
        model.addAttribute("users", userService.findAllUser());
        model.addAttribute("orders", orderRepository.findAll());
        model.addAttribute("orderDetails", orderDetailRepository.findAll());
        model.addAttribute("payments", paymentRepository.findAll());
        model.addAttribute("productInfoImgs", productInfoImgRepository.findAll());
        model.addAttribute("productRepImgs", productRepImgRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("qnas", qnaRepository.findAll());
        model.addAttribute("reviews", reviewRepository.findAll());
        model.addAttribute("reviewImgs", reviewImgRepository.findAll());

        System.err.println("user : ");
        System.err.println(userService.findAllUser());
        System.err.println(userService.findAllUser().get(0).getAccountList());

        return "/test";
    }
}
