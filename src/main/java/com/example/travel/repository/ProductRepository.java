package com.example.travel.repository;

import com.example.travel.domain.Product;
import com.example.travel.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<List<Product>> findAllByUserUserId(long userId);
    // User 의 Page<Product>
    Optional<Page<Product>> findByUser(User user, Pageable pageable);
    // productRegionMainCategory 로 분류해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategory(String mainCategory, Pageable pageable);
    // productRegionMainCategory 로 분류해서 페이징 처리한 User 의 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategoryAndUser(String mainCategory, User user, Pageable pageable);
    // productRegionMainCategory, productRegionSubCategory 로 분류해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategoryAndProductRegionSubCategory(String mainCategory, String subCategory, Pageable pageable);
    // productRegionMainCategory, productRegionSubCategory 로 분류해서 페이징 처리한 User 의 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategoryAndProductRegionSubCategoryAndUser(String mainCategory, String subCategory, User user, Pageable pageable);
    // productStatus 로 분류해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatus(String status, Pageable pageable);
    // productStatus 로 분류해서 페이징 처리한 User 의 Page<Product>
    Optional<Page<Product>> findByProductStatusAndUser(String status, User user, Pageable pageable);
    // productStatus, productRegionMainCategory 로 분류해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategory(String status, String mainCategory, Pageable pageable);
    // productStatus, productRegionMainCategory 로 분류해서 페이징 처리한 User 의 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategoryAndUser(String status, String mainCategory, User user, Pageable pageable);
    // productStatus, productRegionMainCategory, productRegionSubCategory 로 분류해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategoryAndProductRegionSubCategory(String status, String mainCategory, String subCategory, Pageable pageable);
    // productStatus, productRegionMainCategory, productRegionSubCategory 로 분류해서 페이징 처리한 User 의 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategoryAndProductRegionSubCategoryAndUser(String status, String mainCategory, String subCategory, User user, Pageable pageable);
    // productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductTitleContaining(String productTitle, Pageable pageable);
    // productTitle 로 검색해서 페이징 처리한 User 의 Page<Product>
    Optional<Page<Product>> findByProductTitleContainingAndUser(String productTitle, User user, Pageable pageable);
    // productRegionMainCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategoryAndProductTitleContaining(String mainCategory, String productTitle, Pageable pageable);
    // productRegionMainCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 User 의 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategoryAndProductTitleContainingAndUser(String mainCategory, String productTitle, User user, Pageable pageable);
    // productRegionMainCategory, productRegionSubCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategoryAndProductRegionSubCategoryAndProductTitleContaining(String mainCategory, String subCategory, String productTitle, Pageable pageable);
    // productRegionMainCategory, productRegionSubCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 User 의 Page<Product>
    Optional<Page<Product>> findByProductRegionMainCategoryAndProductRegionSubCategoryAndProductTitleContainingAndUser(String mainCategory, String subCategory, String productTitle, User user, Pageable pageable);
    // productStatus 로 분류하고 productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductTitleContaining(String status, String productTitle, Pageable pageable);
    // productStatus 로 분류하고 productTitle 로 검색해서 페이징 처리한 User 의 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductTitleContainingAndUser(String status, String productTitle, User user, Pageable pageable);
    // productStatus, productRegionMainCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategoryAndProductTitleContaining(String status, String mainCategory, String productTitle, Pageable pageable);
    // productStatus, productRegionMainCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 User 의 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategoryAndProductTitleContainingAndUser(String status, String mainCategory, String productTitle, User user, Pageable pageable);
    // productStatus, productRegionMainCategory, productRegionSubCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategoryAndProductRegionSubCategoryAndProductTitleContaining(String status, String mainCategory, String subCategory, String productTitle, Pageable pageable);
    // productStatus, productRegionMainCategory, productRegionSubCategory 로 분류하고 productTitle 로 검색해서 페이징 처리한 User 의 Page<Product>
    Optional<Page<Product>> findByProductStatusAndProductRegionMainCategoryAndProductRegionSubCategoryAndProductTitleContainingAndUser(String status, String mainCategory, String subCategory, String productTitle, User user, Pageable pageable);

    @Query(value = "SELECT * FROM product_tb WHERE product_status = '정상' ORDER BY product_registration_date DESC LIMIT 12", nativeQuery = true)
    Optional<List<Product>> newProduct();
    @Query(value = "SELECT p.* FROM order_detail_tb od JOIN order_tb o ON od.order_id = o.order_id JOIN product_tb p ON o.product_id = p.product_id " +
            "GROUP BY p.product_id ORDER BY SUM(od.order_detail_traveler_count) DESC LIMIT 5;", nativeQuery = true)
    Optional<List<Product>> bestProduct();
    Optional<List<Product>> findAllByProductTitleContaining(String searchText);
    Optional<List<Product>> findAllByProductRegionMainCategory(String mainCategory);
    Optional<List<Product>> findAllByProductRegionSubCategory(String subCategory);
}
