package com.example.travel.repository;

import com.example.travel.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByUserPhone(String userPhone);
    Optional<User> findByUserEmail(String userEmail);
    // 가입회원 페이징 처리하여 조회
    Optional<Page<User>> findByUserDeleteDateIsNull(Pageable pageable);
    // 탈퇴회원 페이징 처리하여 조회
    Optional<Page<User>> findByUserDeleteDateNotNull(Pageable pageable);
    // 전체회원에서 username 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUsernameContaining(String searchKeyword, Pageable pageable);
    // 가입회원에서 username 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUsernameContainingAndUserDeleteDateIsNull(String searchKeyword, Pageable pageable);
    // 탈퇴회원에서 username 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUsernameContainingAndUserDeleteDateNotNull(String searchKeyword, Pageable pageable);
    // 전체회원에서 userRealName 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUserRealNameContaining(String searchKeyword, Pageable pageable);
    // 가입회원에서 userRealName 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUserRealNameContainingAndUserDeleteDateIsNull(String searchKeyword, Pageable pageable);
    // 탈퇴회원에서 userRealName 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUserRealNameContainingAndUserDeleteDateNotNull(String searchKeyword, Pageable pageable);
    // 전체회원에서 userPhone 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUserPhoneContaining(String searchKeyword, Pageable pageable);
    // 가입회원에서 userPhone 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUserPhoneContainingAndUserDeleteDateIsNull(String searchKeyword, Pageable pageable);
    // 탈퇴회원에서 userPhone 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUserPhoneContainingAndUserDeleteDateNotNull(String searchKeyword, Pageable pageable);
    // 전체회원에서 userEmail 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUserEmailContaining(String searchKeyword, Pageable pageable);
    // 가입회원에서 userEmail 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUserEmailContainingAndUserDeleteDateIsNull(String searchKeyword, Pageable pageable);
    // 탈퇴회원에서 userEmail 으로 검색해서 페이징 처리한 Page<User>
    Optional<Page<User>> findByUserEmailContainingAndUserDeleteDateNotNull(String searchKeyword, Pageable pageable);
}
