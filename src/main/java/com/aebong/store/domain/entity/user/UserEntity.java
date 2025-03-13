package com.aebong.store.domain.entity.user;

import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class UserEntity extends BaseEntity {

    @Column(name = "user_id", columnDefinition = "bigint COMMENT '회원순번 PK'", nullable = false)
    @Id
    private Long userId;

    @Column(name = "user_type", columnDefinition = "varchar(20) COMMENT '회원유형'", nullable = false)
    private UserType userType;

    @Column(name = "user_account", columnDefinition = "varchar(30) COMMENT '회원계정'", nullable = false)
    private String userAccount;

    @Column(name = "user_account_type", columnDefinition = "varchar(20) COMMENT '회원계정 유형'", nullable = false)
    private UserAccountType userAccountType;

    @Column(name = "user_password", columnDefinition = "varchar(100) COMMENT '회원 비밀번호'", nullable = false)
    private String userPassword;

    @Column(name = "user_status", columnDefinition = "varchar(20) COMMENT '회원상태 (활성, 탈퇴, 휴면, 잠금'", nullable = false)
    private UserStatus userStatus;

    @Column(name = "password_init_yn", columnDefinition = "char(1) DEFAULT = 'N' COMMENT '비밀번호 초기화 필요 여부'", nullable = false)
    private Boolean passwordInitYn = Boolean.FALSE;

    @Column(name = "fail_password_count", columnDefinition = "int DEFAULT = 0 COMMENT '비밀번호 틀린 횟수'", nullable = false)
    private int failPasswordCount;

    @Column(name = "account_locked_datetime", columnDefinition = "datetime COMMENT '로그인 잠금 일시'")
    private LocalDateTime accountLockedDatetime;

    @Column(name = "last_login_datetime", columnDefinition = "datetime COMMENT '마지막 로그인 일시'")
    private LocalDateTime lastLoginDatetime;

    @Column(name = "last_password_change_datetime", columnDefinition = "datetime COMMENT '마지막 비밀번호 변경 일시'", nullable = false)
    private LocalDateTime lastPasswordChangeDatetime;

    @Column(name = "required_password_change_datetime", columnDefinition = "datetime COMMENT '비밀번호 변경 요구 일시'", nullable = false)
    private LocalDateTime requiredPasswordChangeDatetime;

    @Column(name = "login_available_date", columnDefinition = "date COMMENT '로그인 가능일자'")
    private LocalDate loginAvailableDate;

    @Column(name = "rejoin_possible_date", columnDefinition = "date COMMENT '재가입 가능일자'")
    private LocalDate rejoinPossibleDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_connecting_information_id", columnDefinition = "bigint COMMENT 'CI 인증정보 순번'")
    private UserConnectingInformationEntity userConnectingInformation;

    @Column(name = "rejoin_yn", columnDefinition = "char(1) DEFAULT = 'N' COMMENT '재가입여부'")
    private Boolean isRejoin = Boolean.FALSE;

}
