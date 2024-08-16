package com.example.travel.service;

import com.example.travel.domain.Order;
import com.example.travel.domain.Product;
import com.example.travel.domain.Review;
import com.example.travel.domain.ReviewImg;
import com.example.travel.dto.review.ReviewSubmitRequest;
import com.example.travel.repository.ProductRepository;
import com.example.travel.repository.ReviewImgRepository;
import com.example.travel.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            review = findByReviewId(request.getReviewId()).updateReview(request); // 리뷰 수정
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
            // 상품정보 수정 시 새로 업로드 하는 파일이 있으면 DB 에서 기존 productImg 삭제
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

    @Transactional
    public void deleteReviewByReviewId(long reviewId){
        reviewRepository.deleteById(reviewId);
    }
}
