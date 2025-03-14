package com.aebong.store.domain.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_connecting_information")
@Entity
public class UserConnectingInformationEntity {

    @Column(name = "user_connecting_information_id", columnDefinition = "bigint COMMENT '회원 실명인증 순번 PK'", nullable = false)
    @Id
    private Long userConnectingInformationId;



}
