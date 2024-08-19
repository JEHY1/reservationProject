package com.example.travel.repository;

import com.example.travel.domain.Qna;
import com.example.travel.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // productTitle 으로 검색해서 productTitle 오름차순, reviewId 내림차순으로 페이징 처리한 Page<Review>
    Optional<Page<Review>> findByProductProductTitleContainingOrderByProductProductTitleAscReviewIdDesc(String productTitle, Pageable pageable);
    // reviewTitle 으로 검색해서 reviewId 내림차순으로 페이징 처리한 Page<Review>
    Optional<Page<Review>> findByReviewTitleContainingOrderByReviewIdDesc(String qnaQuestion, Pageable pageable);
    // username 으로 검색해서 username 오름차순, reviewId 내림차순으로 페이징 처리한 Page<Review>
    Optional<Page<Review>> findByOrderUserUsernameContainingOrderByOrderUserUsernameAscReviewIdDesc(String username, Pageable pageable);
    // userRealName 으로 검색해서 userRealName 오름차순, reviewId 내림차순으로 페이징 처리한 Page<Review>
    Optional<Page<Review>> findByOrderUserUserRealNameContainingOrderByOrderUserUserRealNameAscReviewIdDesc(String userRealName, Pageable pageable);
}
