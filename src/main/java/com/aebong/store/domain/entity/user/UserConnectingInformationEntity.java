package com.aebong.store.domain.entity.user;

import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.domain.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_connecting_information")
@Entity
public class UserConnectingInformationEntity extends AuditingEntity {

    @Comment("회원 실명인증 순번 PK")
    @Column(name = "user_connecting_information_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("요청값")
    @Column(name = "request_number", nullable = false, length = 50)
    private String requestNumber;

    @Comment("응답값")
    @Column(name = "response_number", length = 50)
    private String responseNumber;

    @Comment("법정 실명")
    @Column(name = "legal_name", length = 50)
    private String legalName;

    @Comment("법정생년월일")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Comment("법적성별(MALE|FEMALE")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type", length = 20)
    private Gender genderType;

    @Comment("DI 값")
    @Column(name = "duplication_information", length = 100)
    private String duplicationInformation;

    @Comment("CI 값")
    @Column(name = "connecting_information", length = 100)
    private String connectingInformation;

}
