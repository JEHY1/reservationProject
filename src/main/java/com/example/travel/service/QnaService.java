package com.example.travel.service;

import com.example.travel.domain.Qna;
import com.example.travel.dto.admin.QnaAnswerRequest;
import com.example.travel.dto.qna.QnaSubmitRequest;
import com.example.travel.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final UserService userService;
    private final ProductService productService;

    // QnaId로 Qna 가져오기
    public Qna findById(Long qnaId) {
        return qnaRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("not found qna"));
    }

    // 관리자페이지에서 상품문의 조회
    public Page<Qna> qnaList(Integer group, Integer category, String searchKeyword, Pageable pageable) {
        Page<Qna> qnaList = null;
        // 검색 안했을 때
        if(searchKeyword == null || searchKeyword.equals("")) {
            // 전체문의(1) 조회
            if(group == 1) {
                // 전체 상품문의 qnaId 내림차순으로 페이징 처리하여 조회
                qnaList = qnaRepository.findAllByOrderByQnaIdDesc(pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found qna"));
            }
            // 답변미완료(2) 조회
            else if(group == 2) {
                // 답변미완료 상품문의 qnaId 내림차순으로 페이징 처리하여 조회
                qnaList = qnaRepository.findByQnaAnswerIsNullOrderByQnaIdDesc(pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found qna"));
            }
            // 답변완료(2) 조회
            else if(group == 3) {
                // 답변완료 상품문의 qnaId 내림차순으로 페이징 처리하여 조회
                qnaList = qnaRepository.findByQnaAnswerNotNullOrderByQnaIdDesc(pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found qna"));
            }
        }
        // searchKeyword 로 검색 했을 때
        else {
            // 전체문의(1) 조회
            if(group == 1) {
                // category 가 '상품명'일 때
                if(category == 1) {
                    // 전체문의에서 productTitle 으로 검색해서 productTitle 오름차순, qnaId 내림차순으로 페이징 처리한 Page<Qna>
                    qnaList = qnaRepository.findByProductProductTitleContainingOrderByProductProductTitleAscQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
                // category 가 '질문'일 때
                else if(category == 2) {
                    // 전체문의에서 qnaQuestion 으로 검색해서 qnaId 내림차순으로 페이징 처리한 Page<QnA>
                    qnaList = qnaRepository.findByQnaQuestionContainingOrderByQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
                // category 가 '작성자 (아이디)'일 때
                else if(category == 3) {
                    // 전체문의에서 username 으로 검색해서 username 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
                    qnaList = qnaRepository.findByUserUsernameContainingOrderByUserUsernameAscQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
                // category 가 '작성자 (이름)'일 때
                else if(category == 4) {
                    // 전체문의에서 userRealName 으로 검색해서 userRealName 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
                    qnaList = qnaRepository.findByUserUserRealNameContainingOrderByUserUserRealNameAscQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
            }
            // 답변미완료(2) 조회
            else if(group == 2) {
                // category 가 '상품명'일 때
                if(category == 1) {
                    // 답변미완료에서 productTitle 으로 검색해서 productTitle 오름차순, qnaId 내림차순으로 페이징 처리한 Page<Qna>
                    qnaList = qnaRepository.findByQnaAnswerIsNullAndProductProductTitleContainingOrderByProductProductTitleAscQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
                // category 가 '질문'일 때
                else if(category == 2) {
                    // 답변미완료에서 qnaQuestion 으로 검색해서 qnaId 내림차순으로 페이징 처리한 Page<QnA>
                    qnaList = qnaRepository.findByQnaAnswerIsNullAndQnaQuestionContainingOrderByQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
                // category 가 '작성자 (아이디)'일 때
                else if(category == 3) {
                    // 답변미완료에서 username 으로 검색해서 username 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
                    qnaList = qnaRepository.findByQnaAnswerIsNullAndUserUsernameContainingOrderByUserUsernameAscQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
                // category 가 '작성자 (이름)'일 때
                else if(category == 4) {
                    // 답변미완료에서 userRealName 으로 검색해서 userRealName 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
                    qnaList = qnaRepository.findByQnaAnswerIsNullAndUserUserRealNameContainingOrderByUserUserRealNameAscQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
            }
            // 답변완료(3) 조회
            else if(group == 3) {
                // category 가 '상품명'일 때
                if(category == 1) {
                    // 답변완료에서 productTitle 으로 검색해서 productTitle 오름차순, qnaId 내림차순으로 페이징 처리한 Page<Qna>
                    qnaList = qnaRepository.findByQnaAnswerNotNullAndProductProductTitleContainingOrderByProductProductTitleAscQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
                // category 가 '질문'일 때
                else if(category == 2) {
                    // 답변완료에서 qnaQuestion 으로 검색해서 qnaId 내림차순으로 페이징 처리한 Page<QnA>
                    qnaList = qnaRepository.findByQnaAnswerNotNullAndQnaQuestionContainingOrderByQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
                // category 가 '작성자 (아이디)'일 때
                else if(category == 3) {
                    // 답변완료에서 username 으로 검색해서 username 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
                    qnaList = qnaRepository.findByQnaAnswerNotNullAndUserUsernameContainingOrderByUserUsernameAscQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
                // category 가 '작성자 (이름)'일 때
                else if(category == 4) {
                    // 답변완료에서 userRealName 으로 검색해서 userRealName 오름차순, qnaId 내림차순으로 페이징 처리한 Page<QnA>
                    qnaList = qnaRepository.findByQnaAnswerNotNullAndUserUserRealNameContainingOrderByUserUserRealNameAscQnaIdDesc(searchKeyword, pageable)
                            .orElseThrow(() -> new IllegalArgumentException("not found qna"));
                }
            }
        }
        return qnaList;
    }

    // qnaId로 Qna 삭제
    public void deleteById(Long qnaId) {
        qnaRepository.deleteById(qnaId);
    }

    // qnaIds 리스트로 Qna 삭제
    public void deleteByIds(List<Long> qnaIds) {
        for (Long qnaId : qnaIds) {
            qnaRepository.deleteById(qnaId);
        }
    }

    // 상품문의 답변 등록&수정
    @Transactional
    public Qna saveAnswer(QnaAnswerRequest dto) {
        return findById(dto.getQnaId()).saveAnswer(dto);
    }

    // 상품문의 답변 삭제
    @Transactional
    public Qna deleteAnswer(Long qnaId) {
        return findById(qnaId).deleteAnswer();
    }

    public Page<Qna> findQnaByPrincipalWithPage(Principal principal, Pageable pageable){
        return qnaRepository.findByUserUserId(userService.getUserId(principal), pageable)
                .orElseThrow(() -> new IllegalArgumentException("not found qna"));
    }

    public Qna saveQna(QnaSubmitRequest request, Principal principal){
        return qnaRepository.save(Qna.builder()
                .product(productService.findProductByProductId(request.getProductId()))
                .user(userService.getUserByPrincipal(principal))
                .qnaQuestion(request.getQnaQuestion())
                .qnaSecret(request.isQnaSecret())
                .build());
    }
}
