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

    // �α��� �� ����� User Entity ��������
    public User getUserByPrincipal(Principal principal){

        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("not found user"));
    }

    // �α��� �� ����� UserId ��������
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

    // ȸ������ - ȸ������ ���
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

    // userId�� User ��������
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("not found user"));
    }

    // userEmail�� User ��������
    public User findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail).orElse(null);
    }

    // ȸ������ �� ���̵� �ߺ� üũ - join.js�� ajax ����
    public String idCheck(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null) {
            // ���̵� ��밡��
            return "available";
        }
        // ���̵� �ߺ�
        return "duplicated";
    }

    // ȸ������ �� ����ó �ߺ� üũ - join.js�� ajax ����
    public String phoneCheck(String userPhone) {
        User user = userRepository.findByUserPhone(userPhone).orElse(null);
        if(user == null) {
            // �޴�����ȣ ��밡��
            return "available";
        }
        // �޴�����ȣ �ߺ�
        return "duplicated";
    }

    // ȸ������ �� �̸��� �ߺ� üũ - join.js�� ajax ����
    public String emailCheck(String userEmail) {
        User user = findByUserEmail(userEmail);
        if(user == null) {
            // �̸��� ��밡��
            return "available";
        }
        // �̸��� �ߺ�
        return "duplicated";
    }

    // �α��� �� ���̵�, ��й�ȣ Ȯ�� - login.js�� ajax ����
    public String loginCheck(String username, String userPassword) {
        User user = userRepository.findByUsername(username).orElse(null);
        // ���� user �̰ų� Ż���� user �� ���
        if(user == null || user.getUserDeleteDate() != null) {
            // ���̵� ����
            return "error_id";
        }
        else {
            // ��й�ȣ ��ġ Ȯ��
            if(bCryptPasswordEncoder.matches(userPassword, user.getUserPassword())) {
                // �α��� ����
                return "success_login";
            }
            else {
                // ��й�ȣ ����
                return "error_pw";
            }
        }
    }

    // ���� ��й�ȣ ��ġ ���� Ȯ�� - user_information_confirm/modify.js, user_delete_confirm.js�� ajax ����
    public String pwCheck(String userPassword, Principal principal) {
        // ���� ��й�ȣ ��ġ ���� Ȯ��
        boolean pwCheck = bCryptPasswordEncoder.matches(userPassword, getUserByPrincipal(principal).getUserPassword());
        if(pwCheck) {
            // ��й�ȣ ��ġ
            return "accord";
        }
        // ��й�ȣ ����ġ
        return "discord";
    }

    // ȸ������ �������� ��й�ȣ ����
    @Transactional
    public void updatePassword(UpdatePasswordRequest dto, Principal principal) {
        getUserByPrincipal(principal).updatePassword(bCryptPasswordEncoder.encode(dto.getUserPassword()));
    }

    // ȸ������ ����(����ó, �̸���)
    @Transactional
    public void updateUser(UpdateUserRequest dto, Principal principal) {
        getUserByPrincipal(principal).updateUser(dto);
    }

    // ��й�ȣ ã�⿡�� ��й�ȣ �缳��
    @Transactional
    public void resetPassword(ResetPasswordRequest dto) {
        findById(dto.getUserId()).updatePassword(bCryptPasswordEncoder.encode(dto.getUserPassword()));
    }

    // ȸ������ ���� �� ���� ����ó �� �ߺ� üũ - user_information_modify.js�� ajax ����
    public String phoneReCheck(String userPhone, Principal principal) {
        User user = userRepository.findByUserPhone(userPhone).orElse(null);
        if(user == null) {
            // ����ó ��밡��
            return "available";
        }
        else {
            if(userPhone.equals(getUserByPrincipal(principal).getUserPhone())) {
                // ���� ����ó�� ����
                return "equal";
            }
            else {
                // ����ó �ߺ�
                return "duplicated";
            }
        }
    }

    // ȸ������ ���� �� ���� �̸��� �� �ߺ� üũ - user_information_modify.js�� ajax ����
    public String emailReCheck(String userEmail, Principal principal) {
        User user = userRepository.findByUserEmail(userEmail).orElse(null);
        if(user == null) {
            // �̸��� ��밡��
            return "available";
        }
        else {
            if(userEmail.equals(getUserByPrincipal(principal).getUserEmail())) {
                // ���� �̸��ϰ� ����
                return "equal";
            }
            else {
                // �̸��� �ߺ�
                return "duplicated";
            }
        }
    }

    // �α��� �� ����� userDeleteDate �߰��ϱ� - ȸ�� Ż��
    @Transactional
    public void deleteUser(Principal principal) {
        getUserByPrincipal(principal).deleteUser();
    }

}
