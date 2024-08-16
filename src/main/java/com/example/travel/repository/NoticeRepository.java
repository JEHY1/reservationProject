package com.example.travel.repository;

import com.example.travel.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // noticeTitle 로 검색해서 페이징 처리한 Page<Notice>
    Optional<Page<Notice>> findByNoticeTitleContaining(String searchKeyword, Pageable pageable);
    // username 로 검색해서 페이징 처리한 Page<Notice>
    Optional<Page<Notice>> findByUserUsernameContaining(String username, Pageable pageable);
    // userRealName 로 검색해서 페이징 처리한 Page<Notice>
    Optional<Page<Notice>> findByUserUserRealNameContaining(String userRealName, Pageable pageable);
}
