package com.example.travel.repository;

import com.example.travel.domain.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    // productId로 List<ProductOption> 가져오기
    Optional<List<ProductOption>> findByProductProductId(long productId);
}
