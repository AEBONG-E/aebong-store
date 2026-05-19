package com.aebong.store.domain.entity.order;

import com.aebong.store.domain.entity.product.ProductEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_items",
        indexes = @Index(name = "idx_oi_order_id", columnList = "order_id"))
@Entity
public class OrderItemEntity {

    @Comment("주문 아이템 PK")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("주문")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Comment("상품 (삭제 시 NULL 허용 — 스냅샷 보존)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Comment("주문 시점 상품명 스냅샷")
    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Comment("주문 시점 단가 스냅샷")
    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Comment("수량")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Comment("소계 (단가 × 수량)")
    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Builder
    private OrderItemEntity(OrderEntity order, ProductEntity product,
                             String productName, BigDecimal unitPrice, Integer quantity) {
        this.order = order;
        this.product = product;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public static OrderItemEntity create(OrderEntity order, ProductEntity product,
                                         String productName, BigDecimal unitPrice, int quantity) {
        return OrderItemEntity.builder()
                .order(order)
                .product(product)
                .productName(productName)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .build();
    }
}
