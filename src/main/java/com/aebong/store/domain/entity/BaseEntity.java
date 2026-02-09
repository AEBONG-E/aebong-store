package com.aebong.store.domain.entity;

import com.aebong.store.common.util.BooleanToYnConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Comment("삭제여부")
    @Column(name = "delete_yn", nullable = false)
    @Convert(converter = BooleanToYnConverter.class)
    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE;

    @Comment("데이터 생성 일시")
    @Column(name = "created_datetime", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDatetime = LocalDateTime.now();

    @Comment("데이터 수정 일시")
    @Column(name = "modified_datetime")
    @LastModifiedDate
    private LocalDateTime modifiedDatetime;

    protected void setDelete() {
        this.isDeleted = Boolean.TRUE;
    }

}
