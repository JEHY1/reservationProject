package com.example.travel.repository;

import com.example.travel.domain.ProductRepImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepImgRepository extends JpaRepository<ProductRepImg, Long> {

    // productId로 List<ProductRepImg> 가져오기
    Optional<List<ProductRepImg>> findByProductProductId(long productId);
}
