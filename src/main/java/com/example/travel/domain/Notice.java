package com.example.travel.domain;

import com.example.travel.dto.admin.NoticeRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", updatable = false)
    private long noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "notice_title", nullable = false)
    private String noticeTitle;

    @Column(name = "notice_content", nullable = false)
    private String noticeContent;

    @Column(name = "notice_views", nullable = false)
    private int noticeViews;

    @CreatedDate
    @Column(name = "notice_submit_date", nullable = false)
    private LocalDateTime noticeSubmitDate;

    @Builder
    public Notice(User user, String noticeTitle, String noticeContent, int noticeViews) {
        this.user = user;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeViews = noticeViews;
    }

    //공지사항 수정
    public Notice update(NoticeRequest dto) {
        this.noticeTitle = dto.getNoticeTitle();
        this.noticeContent = dto.getNoticeContent();
        return this;
    }

    public Notice updateViewCount(){
        this.noticeViews++;
        return this;
    }
}
