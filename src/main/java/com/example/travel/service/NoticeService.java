package com.example.travel.service;

import com.example.travel.domain.Notice;
import com.example.travel.domain.User;
import com.example.travel.dto.admin.NoticeRequest;
import com.example.travel.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserService userService;

    // noticeId로 Notice 가져오기
    public Notice findById(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("not found notice"));
    }

    // 공지사항 등록&수정
    @Transactional
    public Notice save(NoticeRequest dto, Principal principal) {

        if(dto.getNoticeId() != null) {
            // 공지사항 수정
            return findById(dto.getNoticeId()).update(dto);
        }
        // 공지사항 등록
        return noticeRepository.save(Notice.builder()
                .user(userService.getUserByPrincipal(principal))
                .noticeTitle(dto.getNoticeTitle())
                .noticeContent(dto.getNoticeContent())
                .build());
    }

    // 관리자페이지에서 공지사항 조회
    public Page<Notice> noticeList(Integer category, String searchKeyword, Pageable pageable) {
        Page<Notice> noticeList = null;
        // 검색 안했을 때
        if(searchKeyword == null || searchKeyword.equals("")) {
            noticeList = noticeRepository.findAll(pageable);
        }
        // searchKeyword 로 검색 했을 때
        else {
            // category 가 '제목'일 때
            if(category == 1) {
                // noticeTitle 로 검색해서 페이징 처리한 Page<Notice>
                noticeList = noticeRepository.findByNoticeTitleContaining(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found notice"));
            }
            // category 가 '작성자(아이디)'일 때
            else if(category == 2) {
                // username 로 검색해서 페이징 처리한 Page<Notice>
                noticeList = noticeRepository.findByUserUsernameContaining(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found notice"));
            }
            // category 가 '작성자(이름)'일 때
            else if(category == 3) {
                // userRealName 로 검색해서 페이징 처리한 Page<Notice>
                noticeList = noticeRepository.findByUserUserRealNameContaining(searchKeyword, pageable)
                        .orElseThrow(() -> new IllegalArgumentException("not found notice"));
            }
        }

        return noticeList;
    }

    // noticeId로 Notice 삭제
    public void deleteById(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }

    // noticeIds 리스트로 Notice 삭제
    public void deleteByIds(List<Long> noticeIds) {
        for (Long noticeId : noticeIds) {
            noticeRepository.deleteById(noticeId);
        }
    }

    public Page<Notice> findNoticeWithPage(Pageable pageable){
        return noticeRepository.findAll(pageable);
    }

    @Transactional
    public Notice addNoticeViewsAndGet(long noticeId){
        return findById(noticeId).updateViewCount();
    }
}