package com.aebong.store.domain.entity.user;

import com.aebong.store.common.enums.user.WithdrawalType;
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
@Table(name = "withdrawal_user")
@Entity
public class WithdrawalUserEntity extends AuditingEntity {

    @Comment("탈퇴회원 순번 PK")
    @Column(name = "withdrawal_user_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("회원순번")
    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Comment("탈퇴사유 유형")
    @Enumerated(EnumType.STRING)
    @Column(name = "withdrawal_type", length = 30, nullable = false)
    private WithdrawalType withdrawalType;

    @Column(name = "note", columnDefinition = "TEXT COMMENT '탈퇴 비고'")
    private String note;

    @Comment("탈퇴일시")
    @Column(name = "withdrawal_datetime", nullable = false, updatable = false)
    private LocalDateTime withdrawalDatetime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawalUserEntity that = (WithdrawalUserEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private WithdrawalUserEntity(UserEntity user, WithdrawalType withdrawalType, String note, LocalDateTime withdrawalDatetime) {
        this.user = user;
        this.withdrawalType = withdrawalType;
        this.note = note;
        this.withdrawalDatetime = withdrawalDatetime;
    }

}
