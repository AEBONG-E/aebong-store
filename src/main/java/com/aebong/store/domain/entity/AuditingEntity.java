package com.aebong.store.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class AuditingEntity extends BaseEntity {

    @Comment("데이터 생성 주체")
    @Column(name = "created_user_id", nullable = false, updatable = false)
    @CreatedBy
    private Long createdUserId = 0L; // Todo: 원활한 개발 및 테스트를 위해 임시로 created_user_id 값 설정. 추후 디테일 개발 필요

    @Comment("데이터 수정 주체")
    @Column(name = "modified_user_id")
    @LastModifiedBy
    private Long modifiedUserId;

}
