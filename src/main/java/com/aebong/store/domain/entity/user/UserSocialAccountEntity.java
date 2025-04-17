package com.aebong.store.domain.entity.user;

import com.aebong.store.common.enums.user.SocialServiceType;
import com.aebong.store.domain.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_social_account")
@Entity
public class UserSocialAccountEntity extends AuditingEntity {

    @Comment("회원 소셜계정 순번 PK")
    @Column(name = "user_social_account_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("회원순번")
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Comment("소셜서비스 유형")
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false, length = 20)
    private SocialServiceType serviceType;

    @Comment("소셜서비스 내 회원 고유 식별키")
    @Column(name = "service_id", nullable = false, length = 150)
    private String serviceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSocialAccountEntity that = (UserSocialAccountEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private UserSocialAccountEntity(UserEntity user, SocialServiceType serviceType, String serviceId) {
        this.user = user;
        this.serviceType = serviceType;
        this.serviceId = serviceId;
    }

}
