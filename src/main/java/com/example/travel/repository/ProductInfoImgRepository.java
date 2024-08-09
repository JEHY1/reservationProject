package com.example.travel.repository;

import com.example.travel.domain.ProductInfoImg;
import com.example.travel.domain.ProductRepImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductInfoImgRepository extends JpaRepository<ProductInfoImg, Long> {

    // productId로 List<ProductInfoImg> 가져오기
    Optional<List<ProductInfoImg>> findByProductProductId(long productId);
}