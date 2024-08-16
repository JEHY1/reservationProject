package com.example.travel.repository;

import com.example.travel.domain.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    // 전체 상품문의 qnaId 내림차순으로 페이징 처리하여 조회
    Optional<Page<Qna>> findAllByOrderByQnaIdDesc(Pageable pageable);
    // 답변미완료 상품문의 qnaId 내림차순으로 페이징 처리하여 조회
    Optional<Page<Qna>> findByQnaAnswerIsNullOrderByQnaIdDesc(Pageable pageable);
    // 답변완료 상품문의 qnaId 내림차순으로 페이징 처리하여 조회
    Optional<Page<Qna>> findByQnaAnswerNotNullOrderByQnaIdDesc(Pageable pageable);
    // 전체문의에서 productTitle 으로 검색해서 productTitle 오름차순, qnaId 내림차순으로 페이징 처리한 Page<Qna>
    Optional<Page<Qna>> findByProductProductTitleContainingOrderByProductProductTitleAscQnaIdDesc(String productTitle, Pageable pageable);
    // 전체문의에서 qnaQuestion 으로 검색해서 qnaId 내림차순으로 페이징 처리한 Page<QnA>
    Optional<Page<Qna>> findByQnaQuestionContainingOrderByQnaIdDesc(String qnaQuestion, Pageable pageable);
    // 전체문의에서 username 으로 검색해서 username 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
    Optional<Page<Qna>> findByUserUsernameContainingOrderByUserUsernameAscQnaIdDesc(String username, Pageable pageable);
    // 전체문의에서 userRealName 으로 검색해서 userRealName 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
    Optional<Page<Qna>> findByUserUserRealNameContainingOrderByUserUserRealNameAscQnaIdDesc(String userRealName, Pageable pageable);
    // 답변미완료에서 productTitle 으로 검색해서 productTitle 오름차순, qnaId 내림차순으로 페이징 처리한 Page<Qna>
    Optional<Page<Qna>> findByQnaAnswerIsNullAndProductProductTitleContainingOrderByProductProductTitleAscQnaIdDesc(String productTitle, Pageable pageable);
    // 답변미완료에서 qnaQuestion 으로 검색해서 qnaId 내림차순으로 페이징 처리한 Page<QnA>
    Optional<Page<Qna>> findByQnaAnswerIsNullAndQnaQuestionContainingOrderByQnaIdDesc(String qnaQuestion, Pageable pageable);
    // 답변미완료에서 username 으로 검색해서 username 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
    Optional<Page<Qna>> findByQnaAnswerIsNullAndUserUsernameContainingOrderByUserUsernameAscQnaIdDesc(String username, Pageable pageable);
    // 답변미완료에서 userRealName 으로 검색해서 userRealName 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
    Optional<Page<Qna>> findByQnaAnswerIsNullAndUserUserRealNameContainingOrderByUserUserRealNameAscQnaIdDesc(String userRealName, Pageable pageable);
    // 답변완료에서 productTitle 으로 검색해서 productTitle 오름차순, qnaId 내림차순으로 페이징 처리한 Page<Qna>
    Optional<Page<Qna>> findByQnaAnswerNotNullAndProductProductTitleContainingOrderByProductProductTitleAscQnaIdDesc(String productTitle, Pageable pageable);
    // 답변완료에서 qnaQuestion 으로 검색해서 qnaId 내림차순으로 페이징 처리한 Page<QnA>
    Optional<Page<Qna>> findByQnaAnswerNotNullAndQnaQuestionContainingOrderByQnaIdDesc(String qnaQuestion, Pageable pageable);
    // 답변완료에서 username 으로 검색해서 username 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
    Optional<Page<Qna>> findByQnaAnswerNotNullAndUserUsernameContainingOrderByUserUsernameAscQnaIdDesc(String username, Pageable pageable);
    // 답변완료에서 userRealName 으로 검색해서 userRealName 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
    Optional<Page<Qna>> findByQnaAnswerNotNullAndUserUserRealNameContainingOrderByUserUserRealNameAscQnaIdDesc(String userRealName, Pageable pageable);




}
