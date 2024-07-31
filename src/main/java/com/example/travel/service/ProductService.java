package com.example.travel.service;

import com.example.travel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRepImgRepository productRepImgRepository;
    private final ProductInfoImgRepository productInfoImgRepository;
    private final ProductOptionRepository productOptionRepository;

}
