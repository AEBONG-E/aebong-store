package com.aebong.store.domain.entity.user;

import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.domain.entity.Address;
import com.aebong.store.domain.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_detail")
@Entity
public class UserDetailEntity extends AuditingEntity {

    @Comment("회원 상세정보 순번 PK")
    @Column(name = "user_detail_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("회원순번")
    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Comment("이름")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Comment("성")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Comment("생년월일")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Comment("성별(MALE|FEMALE|NON_BINARY|OTHER")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 20)
    private Gender gender;

    @Comment("휴대폰번호")
    @Column(name = "mobile_number", length = 11)
    private String mobileNumber;

    @Comment("개인식별번호")
    @Column(name = "personal_id", length = 13)
    private String personalId;

    @Comment("닉네임")
    @Column(name = "nick_name", length = 100)
    private String nickName;

    @Comment("전화번호")
    @Column(name = "tel_number", length = 11)
    private String telNumber;

    @Comment("이메일")
    @Column(name = "email", length = 150)
    private String email;

    @Embedded
    private Address address;

    @Comment("회원가입 일시")
    @Column(name = "join_datetime", nullable = false)
    private LocalDateTime joinDatetime;

    @Comment("계정 활성화 일시 - 최초 가입시, 탈퇴 -> 복원 시, 휴면 -> 복구 시")
    @Column(name = "activated_datetime", nullable = false)
    private LocalDateTime activatedDatetime;

    @Comment("계정 비활성 일시")
    @Column(name = "inactivated_datetime")
    private LocalDateTime inactivatedDatetime;

    @Comment("탈퇴 일시")
    @Column(name = "withdrawal_datetime")
    private LocalDateTime withdrawalDatetime;

    @Comment("휴면처리 일시")
    @Column(name = "dormant_datetime")
    private LocalDateTime dormantDatetime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailEntity that = (UserDetailEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private UserDetailEntity(UserEntity user, String firstName, String lastName, LocalDate birthDate, Gender gender, String mobileNumber, String personalId, String nickName, String telNumber, String email, Address address, LocalDateTime joinDatetime, LocalDateTime activatedDatetime, LocalDateTime inactivatedDatetime, LocalDateTime withdrawalDatetime, LocalDateTime dormantDatetime) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.personalId = personalId;
        this.nickName = nickName;
        this.telNumber = telNumber;
        this.email = email;
        this.address = address;
        this.joinDatetime = joinDatetime;
        this.activatedDatetime = activatedDatetime;
        this.inactivatedDatetime = inactivatedDatetime;
        this.withdrawalDatetime = withdrawalDatetime;
        this.dormantDatetime = dormantDatetime;
    }

}
