package com.aebong.store.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "delete_yn", columnDefinition = "char(1) DEFAULT = 'N' COMMENT '삭제여부'", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    @CreatedBy
    @Column(name = "created_user_id", columnDefinition = "bigint COMMENT '데이터 생성 주체'", nullable = false, updatable = false)
    private Long createdUserId;

    @CreatedDate
    @Column(name = "created_datetime", columnDefinition = "datetime COMMENT '데이터 생성 일시'", nullable = false, updatable = false)
    private LocalDateTime createdDatetime;

    @LastModifiedBy
    @Column(name = "modified_user_id", columnDefinition = "bigint COMMENT '데이터 수정 주체'")
    private Long modifiedUserId;

    @LastModifiedDate
    @Column(name = "modified_datetime", columnDefinition = "datetime COMMENT '데이터 수정 일시'")
    private LocalDateTime modifiedDatetime;

}
