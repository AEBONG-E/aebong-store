package com.aebong.store.domain.entity.user;

import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.domain.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`users`")
@Entity
public class UserEntity extends AuditingEntity {

    @Comment("회원순번 PK")
    @Column(name = "user_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("회원유형")
    @Column(name = "user_type", nullable = false, length = 20)
    private UserType userType;

    @Comment("회원계정")
    @Column(name = "user_account", nullable = false, length = 30, unique = true)
    private String userAccount;

    @Comment("회원계정 유형")
    @Column(name = "user_account_type", nullable = false, length = 20)
    private UserAccountType userAccountType;

    @Comment("비밀번호")
    @Column(name = "user_password", nullable = false, length = 100)
    private String userPassword;

    @Comment("회원상태 (활성|탈퇴|휴면|잠금)")
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false, length = 20)
    private UserStatus userStatus;

    @Comment("비밀번호 초기화 필요 여부")
    @Column(name = "password_init_yn", nullable = false)
    private Boolean passwordInitYn;

    @Comment("비밀번호 틀린 횟수")
    @Column(name = "fail_password_count", nullable = false)
    private Integer failPasswordCount;

    @Comment("로그인 잠금 일시")
    @Column(name = "account_locked_datetime")
    private LocalDateTime accountLockedDatetime;

    @Comment("마지막 로그인 일시")
    @Column(name = "last_login_datetime")
    private LocalDateTime lastLoginDatetime;

    @Comment("마지막 비밀번호 변경 일시")
    @Column(name = "last_password_change_datetime", nullable = false)
    private LocalDateTime lastPasswordChangeDatetime;

    @Comment("비밀번호 변경 요구 일시")
    @Column(name = "required_password_change_datetime", nullable = false)
    private LocalDateTime requiredPasswordChangeDatetime;

    @Comment("로그인 가능일자")
    @Column(name = "login_available_date")
    private LocalDate loginAvailableDate;

//    @Comment("재가입 가능일자")
//    @Column(name = "rejoin_possible_date")
//    private LocalDate rejoinPossibleDate;

//    todo: NICE 인증 모듈 구현 시 추가 예정
//    @Comment("CI 인증정보 순번")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_connecting_information_id")
//    private UserConnectingInformationEntity userConnectingInformation;

//    @Comment("재가입여부")
//    @Column(name = "rejoin_yn", nullable = false)
//    private Boolean isRejoin;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInformationChangeHistoryEntity> userInformationChangeHistories = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private UserDetailEntity userDetail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private UserEntity(UserType userType,
                       String userAccount,
                       UserAccountType userAccountType,
                       String userPassword,
                       UserStatus userStatus,
                       Boolean passwordInitYn,
                       int failPasswordCount,
                       LocalDateTime accountLockedDatetime,
                       LocalDateTime lastLoginDatetime,
                       LocalDateTime lastPasswordChangeDatetime,
                       LocalDateTime requiredPasswordChangeDatetime,
                       LocalDate loginAvailableDate,
                       LocalDate rejoinPossibleDate,
                       Boolean isRejoin)
    {
        this.userType = userType;
        this.userAccount = userAccount;
        this.userAccountType = userAccountType;
        this.userPassword = userPassword;
        this.userStatus = userStatus;
        this.passwordInitYn = Objects.isNull(passwordInitYn) ? Boolean.FALSE : passwordInitYn;
        this.failPasswordCount = Objects.isNull(failPasswordCount) ? 0 : failPasswordCount;
        this.accountLockedDatetime = accountLockedDatetime;
        this.lastLoginDatetime = lastLoginDatetime;
        this.lastPasswordChangeDatetime = Objects.isNull(lastPasswordChangeDatetime) ? LocalDateTime.now() : lastPasswordChangeDatetime;
        this.requiredPasswordChangeDatetime = Objects.isNull(requiredPasswordChangeDatetime) ? LocalDateTime.now().plusMonths(6) : requiredPasswordChangeDatetime;
        this.loginAvailableDate = loginAvailableDate;
//        this.rejoinPossibleDate = rejoinPossibleDate;
//        this.isRejoin = Objects.isNull(isRejoin) ? Boolean.FALSE : isRejoin;
    }

}
