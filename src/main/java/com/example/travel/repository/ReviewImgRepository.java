package com.example.travel.repository;

import com.example.travel.domain.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long>{

    // reviewId로 List<ReviewImg> 가져오기
    Optional<List<ReviewImg>> findByReviewReviewId(long reviewId);
}
