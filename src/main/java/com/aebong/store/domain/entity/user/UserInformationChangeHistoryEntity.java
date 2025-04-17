package com.aebong.store.domain.entity.user;

import com.aebong.store.common.enums.user.LogSubType;
import com.aebong.store.common.enums.user.LogType;
import com.aebong.store.domain.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_information_change_history")
@Entity
public class UserInformationChangeHistoryEntity extends AuditingEntity {

    @Comment("회원정보 변경이력 순번 PK")
    @Column(name = "user_information_change_history_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("회원순번")
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Comment("변경항목 유형 (대분류)")
    @Enumerated(EnumType.STRING)
    @Column(name = "log_type", length = 50)
    private LogType logType;

    @Comment("변경항목 유형 (소분류)")
    @Enumerated(EnumType.STRING)
    @Column(name = "log_sub_type", length = 50)
    private LogSubType logSubType;

    @Column(name = "description", columnDefinition = "TEXT COMMENT '이력 설명'")
    private String description;

    @Column(name = "note", columnDefinition = "TEXT COMMENT '이력 비고'")
    private String note;

    @Comment("정보변경일시")
    @Column(name = "occurrence_datetime", nullable = false)
    private LocalDateTime occurrenceDatetime;

    @Comment("변경전")
    @Column(name = "before_change")
    private String before;

    @Comment("변경후")
    @Column(name = "after_change")
    private String after;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInformationChangeHistoryEntity that = (UserInformationChangeHistoryEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private UserInformationChangeHistoryEntity(UserEntity user, LogType logType, LogSubType logSubType, String description, String note, LocalDateTime occurrenceDatetime, String before, String after) {
        this.user = user;
        this.logType = logType;
        this.logSubType = logSubType;
        this.description = description;
        this.note = note;
        this.occurrenceDatetime = occurrenceDatetime;
        this.before = before;
        this.after = after;
    }

}
