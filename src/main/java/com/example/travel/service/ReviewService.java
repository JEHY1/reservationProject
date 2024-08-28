package com.example.travel.service;

import com.example.travel.domain.*;
import com.example.travel.dto.admin.ReviewCommentRequest;
import com.example.travel.dto.review.ReviewSubmitRequest;
import com.example.travel.repository.ReviewImgRepository;
import com.example.travel.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final PaymentService paymentService;

    public Review findByReviewId(long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("not found review"));
    }

    @Transactional
    public void saveReview(ReviewSubmitRequest request) {
        Review review = null;

        if(request.getReviewId() != null) {
            // 리뷰 수정
            review = findByReviewId(request.getReviewId()).updateReview(request);
        }
        else {
            Order order = paymentService.findOrderByOrderId(request.getOrderId());
            review = Review.builder()
                    .product(order.getProduct())
                    .order(order)
                    .reviewTitle(request.getReviewTitle())
                    .reviewContent(request.getReviewContent())
                    .reviewScore(request.getReviewScore())
                    .build();

            reviewRepository.save(review);
        }

        // files 등록할 경우 저장
        if(!request.getFiles().get(0).isEmpty()) {
            // 리뷰 수정 시 새로 업로드 하는 파일이 있으면 DB 에서 기존 reviewImg 삭제
            if(request.getReviewId() != null) {

                review.getReviewImgList().forEach(reviewImg -> reviewImgRepository.deleteById(reviewImg.getReviewImgId()));
            }

            int index = 1;
            for (MultipartFile file : request.getFiles()) {

                String originalFileName = file.getOriginalFilename();
                //파일 확장자 추출
                int extensionIndex = originalFileName.lastIndexOf(".");
                String extension = originalFileName.substring(extensionIndex);

                ReviewImg reviewImg = ReviewImg.builder()
                        .review(review)
                        .reviewImgSrc("review" + review.getReviewId() + "_" + index + extension)
                        .build();
                reviewImgRepository.save(reviewImg);

                //파일업로드
                fileUpload(file, review.getReviewId(), extension, "reviewImg", index++);
            }
        }


    }

    //파일 업로드(파일, 상품 아이디, 파일 확장자, 추가 경로, 파일 번호)
    public void fileUpload(MultipartFile multipartFile, long reviewId, String extension, String dir, Integer num) {

        //경로 만들기
        Path copyOfLocation = Paths.get(uploadDir + File.separator + dir + File.separator + "review" + reviewId + "_" + (num == null ? "" : num) + extension);

        System.err.println(copyOfLocation.toString());
        try {
            // inputStream 사용
            // copyOfLocation 저장위치
            // 기존 파일이 존재할 경우 덮어쓰기
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new IllegalArgumentException("Could not store file : " + multipartFile.getOriginalFilename());
        }
    }

    // 관리자페이지에서 리뷰 조회
    public Page<Review> reviewList(Integer category, String searchKeyword, Pageable pageable) {
        Page<Review> reviewList = null;

        // 검색 안했을 때
        if(searchKeyword == null || searchKeyword.equals("")) {
            reviewList = reviewRepository.findAllByOrderByReviewIdDesc(pageable)
                    .orElseThrow(() -> new IllegalArgumentException("not found review"));
        }
        // searchKeyword 로 검색 했을 때
        else {
            // category 가 '상품명'일 때
            if(category == 1) {
                // productTitle 으로 검색해서 productTitle 오름차순, reviewId 내림차순으로 페이징 처리한 Page<Review>
                reviewList = reviewRepository.findByProductProductTitleContainingOrderByProductProductTitleAscReviewIdDesc(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found review"));
            }
            // category 가 '제목'일 때
            else if(category == 2) {
                // reviewTitle 으로 검색해서 reviewId 내림차순으로 페이징 처리한 Page<Review>
                reviewList = reviewRepository.findByReviewTitleContainingOrderByReviewIdDesc(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found review"));
            }
            // category 가 '작성자 (아이디)'일 때
            else if(category == 3) {
                reviewList = reviewRepository.findByOrderUserUsernameContainingOrderByOrderUserUsernameAscReviewIdDesc(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found review"));
            }
            // category 가 '작성자 (이름)'일 때
            else if(category == 4) {
                reviewList = reviewRepository.findByOrderUserUserRealNameContainingOrderByOrderUserUserRealNameAscReviewIdDesc(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found review"));
            }
        }
        return reviewList;
    }

    public void deleteReviewByReviewId(long reviewId){
        reviewRepository.deleteById(reviewId);
    }

    // reviewIds 리스트로 Review 삭제
    public void deleteReviewsByReviewIds(List<Long> reviewIds) {
        for (Long reviewId : reviewIds) {
            reviewRepository.deleteById(reviewId);
        }
    }

    // reviewId로 List<ReviewImg> 가져오기
    public List<ReviewImg> FindReviewImgsByReviewId(long reviewId) {
        return reviewImgRepository.findByReviewReviewId(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("not found reviewimgs"));
    }

    // 리뷰 답글 등록&수정
    @Transactional
    public Review saveComment(ReviewCommentRequest dto) {
        return findByReviewId(dto.getReviewId()).saveComment(dto);
    }

    // 리뷰 답글 삭제
    @Transactional
    public Review deleteComment(long reviewId) {
        return findByReviewId(reviewId).deleteComment();
    }
}
