package com.aebong.store.domain.entity.user;

import com.aebong.store.common.util.BooleanToYnConverter;
import com.aebong.store.domain.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "refresh_token",
        uniqueConstraints = @UniqueConstraint(name = "uk_refresh_token_hash", columnNames = "token_hash")
)
@Entity
public class RefreshTokenEntity extends AuditingEntity {

    @Comment("리프레시 토큰 PK")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id", nullable = false)
    private Long id;

    @Comment("회원계정")
    @Column(name = "user_account", nullable = false, length = 30)
    private String userAccount;

    @Comment("리프레시 토큰 해시")
    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Comment("만료일시")
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Comment("폐기여부")
    @Convert(converter = BooleanToYnConverter.class)
    @Column(name = "revoked_yn", nullable = false)
    private Boolean isRevoked;

    @Builder
    private RefreshTokenEntity(String userAccount, String tokenHash, LocalDateTime expiresAt, Boolean isRevoked) {
        this.userAccount = userAccount;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.isRevoked = isRevoked == null ? Boolean.FALSE : isRevoked;
    }

    public void revoke() {
        this.isRevoked = Boolean.TRUE;
    }
}
