package com.example.travel.repository;

import com.example.travel.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<List<Product>> findAllByUserUserId(long userId);
    // productRegionMainCategory 로 분류해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategory(String mainCategory, Pageable pageable);
    // productRegionMainCategory, productRegionSubCategory 로 분류해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategoryAndProductRegionSubCategory(String mainCategory, String subCategory, Pageable pageable);
    // productStatus 로 분류해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatus(String status, Pageable pageable);
    // productStatus, productRegionMainCategory 로 분류해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategory(String status, String mainCategory, Pageable pageable);
    // productStatus, productRegionMainCategory, productRegionSubCategory 로 분류해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategoryAndProductRegionSubCategory(String status, String mainCategory, String subCategory, Pageable pageable);
    // productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductTitleContaining(String productTitle, Pageable pageable);
    // productRegionMainCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategoryAndProductTitleContaining(String mainCategory, String productTitle, Pageable pageable);
    // productRegionMainCategory, productRegionSubCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategoryAndProductRegionSubCategoryAndProductTitleContaining(String mainCategory, String subCategory, String productTitle, Pageable pageable);
    // productStatus 로 분류하고 productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductTitleContaining(String status, String productTitle, Pageable pageable);
    // productStatus, productRegionMainCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategoryAndProductTitleContaining(String status, String mainCategory, String productTitle, Pageable pageable);
    // productStatus, productRegionMainCategory, productRegionSubCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategoryAndProductRegionSubCategoryAndProductTitleContaining(String status, String mainCategory, String subCategory, String productTitle, Pageable pageable);

}
