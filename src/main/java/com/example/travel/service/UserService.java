package com.example.travel.service;

import com.example.travel.dto.login.ResetPasswordRequest;
import com.example.travel.domain.Account;
import com.example.travel.domain.User;
import com.example.travel.dto.login.JoinRequest;
import com.example.travel.dto.login.UpdatePasswordRequest;
import com.example.travel.dto.login.UpdateUserRequest;
import com.example.travel.repository.AccountRepository;
import com.example.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 로그인 된 사용자 User Entity 가져오기
    public User getUserByPrincipal(Principal principal){

        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("not found user"));
    }

    // 로그인 된 사용자 UserId 가져오기
    public long getUserId(Principal principal){
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("not found user")).getUserId();
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public List<Account> findAllAccount(){
        return accountRepository.findAll();
    }

    // 회원가입 - 회원정보 등록
    public Long save(JoinRequest dto) {
        return userRepository.save(User.builder()
                .username(dto.getUsername())
                .userPassword(bCryptPasswordEncoder.encode(dto.getUserPassword()))
                .userRealName(dto.getUserRealName())
                .userPhone(dto.getUserPhone1() + "-" + dto.getUserPhone2() + "-" + dto.getUserPhone3())
                .userEmail(dto.getUserEmail())
                .userSmsAgreement(dto.getUserSmsAgreement())
                .userEmailAgreement(dto.getUserEmailAgreement())
                .userRole("COSTOMER")
                .build()).getUserId();
    }

    // userId로 User 가져오기
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("not found user"));
    }

    // userEmail로 User 가져오기
    public User findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail).orElse(null);
    }

    // 회원가입 시 아이디 중복 체크 - join.js에 ajax 연결
    public String idCheck(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null) {
            // 아이디 사용가능
            return "available";
        }
        // 아이디 중복
        return "duplicated";
    }

    // 회원가입 시 연락처 중복 체크 - join.js에 ajax 연결
    public String phoneCheck(String userPhone) {
        User user = userRepository.findByUserPhone(userPhone).orElse(null);
        if(user == null) {
            // 휴대폰번호 사용가능
            return "available";
        }
        // 휴대폰번호 중복
        return "duplicated";
    }

    // 회원가입 시 이메일 중복 체크 - join.js에 ajax 연결
    public String emailCheck(String userEmail) {
        User user = findByUserEmail(userEmail);
        if(user == null) {
            // 이메일 사용가능
            return "available";
        }
        // 이메일 중복
        return "duplicated";
    }

    // 로그인 시 아이디, 비밀번호 확인 - login.js에 ajax 연결
    public String loginCheck(String username, String userPassword) {
        User user = userRepository.findByUsername(username).orElse(null);
        // 없는 user 이거나 탈퇴한 user 일 경우
        if(user == null || user.getUserDeleteDate() != null) {
            // 아이디 오류
            return "error_id";
        }
        else {
            // 비밀번호 일치 확인
            if(bCryptPasswordEncoder.matches(userPassword, user.getUserPassword())) {
                // 로그인 성공
                return "success_login";
            }
            else {
                // 비밀번호 오류
                return "error_pw";
            }
        }
    }

    // 기존 비밀번호 일치 여부 확인 - user_information_confirm/modify.js, user_delete_confirm.js에 ajax 연결
    public String pwCheck(String userPassword, Principal principal) {
        // 기존 비밀번호 일치 여부 확인
        boolean pwCheck = bCryptPasswordEncoder.matches(userPassword, getUserByPrincipal(principal).getUserPassword());
        if(pwCheck) {
            // 비밀번호 일치
            return "accord";
        }
        // 비밀번호 불일치
        return "discord";
    }

    // 회원정보 수정에서 비밀번호 변경
    @Transactional
    public void updatePassword(UpdatePasswordRequest dto, Principal principal) {
        getUserByPrincipal(principal).updatePassword(bCryptPasswordEncoder.encode(dto.getUserPassword()));
    }

    // 회원정보 수정(연락처, 이메일)
    @Transactional
    public void updateUser(UpdateUserRequest dto, Principal principal) {
        getUserByPrincipal(principal).updateUser(dto);
    }

    // 비밀번호 찾기에서 비밀번호 재설정
    @Transactional
    public void resetPassword(ResetPasswordRequest dto) {
        findById(dto.getUserId()).updatePassword(bCryptPasswordEncoder.encode(dto.getUserPassword()));
    }

    // 회원정보 수정 시 기존 연락처 및 중복 체크 - user_information_modify.js에 ajax 연결
    public String phoneReCheck(String userPhone, Principal principal) {
        User user = userRepository.findByUserPhone(userPhone).orElse(null);
        if(user == null) {
            // 연락처 사용가능
            return "available";
        }
        else {
            if(userPhone.equals(getUserByPrincipal(principal).getUserPhone())) {
                // 기존 연락처와 동일
                return "equal";
            }
            else {
                // 연락처 중복
                return "duplicated";
            }
        }
    }

    // 회원정보 수정 시 기존 이메일 및 중복 체크 - user_information_modify.js에 ajax 연결
    public String emailReCheck(String userEmail, Principal principal) {
        User user = userRepository.findByUserEmail(userEmail).orElse(null);
        if(user == null) {
            // 이메일 사용가능
            return "available";
        }
        else {
            if(userEmail.equals(getUserByPrincipal(principal).getUserEmail())) {
                // 기존 이메일과 동일
                return "equal";
            }
            else {
                // 이메일 중복
                return "duplicated";
            }
        }
    }

    // 로그인 된 사용자 userDeleteDate 추가하기 - 회원 탈퇴
    @Transactional
    public void deleteUser(Principal principal) {
        getUserByPrincipal(principal).deleteUser();
    }

}
