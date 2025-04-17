package com.aebong.store.domain.entity.product;

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
@Table(name = "product_category")
@Entity
public class ProductCategoryEntity extends AuditingEntity {

    @Comment("상품 카테고리 순번 PK")
    @Column(name = "product_category_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("상품순번")
    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    @Comment("카테고리 순번")
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @Comment("대표 카테고리 여부")
    @Column(name = "main_category_yn", nullable = false)
    private Boolean isMainCategory = Boolean.FALSE;

    @Comment("버전")
    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategoryEntity that = (ProductCategoryEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private ProductCategoryEntity(ProductEntity product, CategoryEntity category, Boolean isMainCategory, Integer version) {
        this.product = product;
        this.category = category;
        this.isMainCategory = isMainCategory;
        this.version = version;
    }

}
