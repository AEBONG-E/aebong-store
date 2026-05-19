package com.aebong.store.domain.entity.order;

import com.aebong.store.common.enums.order.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_status_histories",
        indexes = @Index(name = "idx_osh_order_id", columnList = "order_id"))
@Entity
public class OrderStatusHistoryEntity {

    @Comment("주문 상태 이력 PK")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("주문")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Comment("변경된 주문 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OrderStatus status;

    @Comment("비고")
    @Column(name = "note", length = 500)
    private String note;

    @Comment("상태 변경 일시")
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt = LocalDateTime.now();

    @Builder
    private OrderStatusHistoryEntity(OrderEntity order, OrderStatus status, String note) {
        this.order = order;
        this.status = status;
        this.note = note;
    }

    public static OrderStatusHistoryEntity record(OrderEntity order, OrderStatus status, String note) {
        return OrderStatusHistoryEntity.builder()
                .order(order)
                .status(status)
                .note(note)
                .build();
    }
}
