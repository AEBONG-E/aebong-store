package com.aebong.store.domain.entity.cart;

import com.aebong.store.domain.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "carts", uniqueConstraints = @UniqueConstraint(columnNames = "user_account"))
@Entity
public class CartEntity {

    @Comment("장바구니 PK")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("회원 계정 (1인 1장바구니)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account", referencedColumnName = "user_account", nullable = false)
    private UserEntity user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemEntity> items = new ArrayList<>();

    @Builder
    private CartEntity(UserEntity user) {
        this.user = user;
    }

    public static CartEntity create(UserEntity user) {
        return CartEntity.builder().user(user).build();
    }

    public void clearItems() {
        this.items.clear();
    }
}
