package com.aebong.store.domain.entity.product;

import com.aebong.store.common.enums.product.ProductType;
import com.aebong.store.domain.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

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

    @OneToMany(mappedBy = "product")
    private List<PriceEntity> prices = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ImageEntity> images = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductCategoryEntity> productCategories = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductTagEntity> productTags = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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
                          List<ProductCategoryEntity> productCategories,
                          List<ProductTagEntity> productTags) {
        this.productCode = productCode;
        this.productType = productType;
        this.productCategories = productCategories;
        this.productTags = productTags;
    }

}
