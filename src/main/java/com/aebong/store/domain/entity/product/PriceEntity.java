package com.aebong.store.domain.entity.product;

import com.aebong.store.common.enums.product.DiscountType;
import com.aebong.store.domain.entity.AuditingEntity;
import com.aebong.store.service.product.dto.ProductRegisterInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "price")
@Entity
public class PriceEntity extends AuditingEntity {

    @Comment("상품가격순번 PK")
    @Column(name = "price_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("상품순번")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Comment("적용시작일자(YYYYMMDD)")
    @Column(name = "apply_start_date", nullable = false)
    private LocalDate applyStartDate;

    @Comment("적용종료일자(YYYYMMDD)")
    @Column(name = "apply_end_date", nullable = false)
    private LocalDate applyEndDate;

    @Comment("상품 판매금액")
    @Column(name = "sales_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal salesAmount;

    @Comment("상품 매입금액")
    @Column(name = "purchase_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal purchaseAmount;

    @Comment("할인유형")
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType;

    @Comment("할인율 적용가")
    @Column(name = "discount_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal discountAmount;

    @Comment("할인(율 or 금액)")
    @Column(name = "discount", nullable = false)
    private Long discount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceEntity that = (PriceEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Builder
    public PriceEntity(ProductEntity product,
                       LocalDate applyStartDate,
                       LocalDate applyEndDate,
                       BigDecimal salesAmount,
                       BigDecimal purchaseAmount,
                       DiscountType discountType,
                       BigDecimal discountAmount,
                       Long discount) {
        this.product = product;
        this.applyStartDate = applyStartDate;
        this.applyEndDate = applyEndDate;
        this.salesAmount = salesAmount;
        this.purchaseAmount = purchaseAmount;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.discount = discount;
    }

    public static PriceEntity create(ProductEntity product, ProductRegisterInfo registerInfo) {
        if (Objects.isNull(product) || Objects.isNull(registerInfo)) return null;
        return PriceEntity.builder()
                .product(product)
                .applyStartDate(registerInfo.getApplyStartDate())
                .applyEndDate(registerInfo.getApplyEndDate())
                .salesAmount(registerInfo.getSalesAmount())
                .purchaseAmount(registerInfo.getPurchaseAmount())
                .discountType(registerInfo.getDiscountType())
                .discountAmount(registerInfo.getDiscountAmount())
                .discount(registerInfo.getDiscount())
                .build();
    }

}
