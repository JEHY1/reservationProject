package com.example.travel.domain;

import com.example.travel.dto.admin.AdminUserRequest;
import com.example.travel.dto.login.UpdateUserRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private long userId;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_real_name", nullable = false)
    private String userRealName;

    @Column(name = "user_phone", nullable = false, unique = true)
    private String userPhone;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @CreatedDate
    @Column(name = "user_submit_date", nullable = false)
    private LocalDateTime userSubmitDate;

    @Column(name = "user_delete_date")
    private LocalDateTime userDeleteDate;

    @Column(name = "user_sms_agreement")
    private Boolean userSmsAgreement;

    @Column(name = "user_email_agreement")
    private Boolean userEmailAgreement;

    @Column(name = "user_role", nullable = false)
    private String userRole;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Account> accountList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole));
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Builder
    public User(String username, String userPassword, String userRealName, String userPhone, String userEmail, Boolean userSmsAgreement, Boolean userEmailAgreement, String userRole) {
        this.username = username;
        this.userPassword = userPassword;
        this.userRealName = userRealName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userSmsAgreement = userSmsAgreement;
        this.userEmailAgreement = userEmailAgreement;
        this.userRole = userRole;
    }

    // 회원정보 수정에서 비밀번호 변경
    public void updatePassword(String userPassword) {
        this.userPassword = userPassword;
    }

    // 회원정보 수정(연락처, 이메일)
    public void updateUser(UpdateUserRequest dto) {
        this.userPhone = dto.getUserPhone();
        this.userEmail = dto.getUserEmail();
        this.userSmsAgreement = dto.getUserSmsAgreement();
        this.userEmailAgreement = dto.getUserEmailAgreement();
    }

    // userDeleteDate 추가하기 - 회원 탈퇴
    public void deleteUser() {
        this.userDeleteDate = this.userDeleteDate == null? LocalDateTime.now() : this.userDeleteDate;
    }

    // 관리자 페이지에서 회원정보 수정
    public User updateAdminUser(AdminUserRequest dto) {
        this.userPassword = dto.getUserPassword();
        this.userRealName = dto.getUserRealName();
        this.userPhone = dto.getUserPhone();
        this.userEmail = dto.getUserEmail();
        this.userRole = dto.getUserRole();
        this.userSmsAgreement = dto.getUserSmsAgreement();
        this.userEmailAgreement = dto.getUserEmailAgreement();
        return this;
    }
}
