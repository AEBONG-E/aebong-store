package com.aebong.store.domain.entity.product;

import com.aebong.store.common.enums.product.CategoryType;
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
@Table(name = "category")
@Entity
public class CategoryEntity extends AuditingEntity {

    @Comment("카테고리 PK")
    @Column(name = "category_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("상위 카테고리 순번")
    @JoinColumn(name = "upper_category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity upperCategory;

    @Comment("카테고리 유형")
    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false, length = 20)
    private CategoryType categoryType;

    @Comment("카테고리 코드")
    @Enumerated(EnumType.STRING)
    @Column(name = "category_code", nullable = false, length = 20)
    private CategoryType categoryCode;

    @Comment("카테고리명")
    @Enumerated(EnumType.STRING)
    @Column(name = "category_name", nullable = false, length = 50)
    private CategoryType categoryName;

    @OneToMany(mappedBy = "category")
    private List<ProductCategoryEntity> productCategories = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private CategoryEntity(CategoryEntity upperCategory, CategoryType categoryType, CategoryType categoryCode, CategoryType categoryName, List<ProductCategoryEntity> productCategories) {
        this.upperCategory = upperCategory;
        this.categoryType = categoryType;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.productCategories = productCategories;
    }

}
