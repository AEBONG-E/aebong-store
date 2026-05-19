package com.aebong.store.domain.entity.product;

import com.aebong.store.common.enums.product.ProductType;
import com.aebong.store.domain.entity.AuditingEntity;
import com.aebong.store.service.product.dto.ProductModifyRequest;
import com.aebong.store.service.product.dto.ProductRegisterInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class ProductEntity extends AuditingEntity {

    @Comment("상품순번 PK")
    @Column(name = "product_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("상품코드")
    @Column(name = "product_code", nullable = false, length = 20)
    private String productCode;

    @Comment("상품유형")
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 100)
    private ProductType productType;

    @Comment("재고수량")
    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Comment("삭제일시")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "product")
    private List<PriceEntity> prices = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ImageEntity> images = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductCategoryEntity> productCategories = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductTagEntity> productTags = new ArrayList<>();

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductDetailEntity productDetail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private ProductEntity(String productCode,
                          ProductType productType,
                          Integer stock,
                          List<ProductCategoryEntity> productCategories,
                          List<ProductTagEntity> productTags) {
        this.productCode = productCode;
        this.productType = productType;
        this.stock = Objects.isNull(stock) ? 0 : stock;
        this.productCategories = productCategories;
        this.productTags = productTags;
    }

    public static ProductEntity create(ProductRegisterInfo registerInfo) {
        if (Objects.isNull(registerInfo)) return null;
        return ProductEntity.builder()
                .productCode(registerInfo.getProductCode())
                .productType(registerInfo.getProductType())
                .build();
    }

    public void modify(ProductModifyRequest request) {
        if (Objects.nonNull(request.getProductType())) {
            this.productType = request.getProductType();
        }
        if (Objects.nonNull(request.getStock())) {
            this.stock = request.getStock();
        }
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        setDelete();
    }

}
