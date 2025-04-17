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
@Table(name = "product_tag")
@Entity
public class ProductTagEntity extends AuditingEntity {

    @Comment("상품 태그 순번 PK")
    @Column(name = "product_tag_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("상품순번")
    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    @Comment("태그순번")
    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TagEntity tag;

    @Comment("버전")
    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductTagEntity that = (ProductTagEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private ProductTagEntity(ProductEntity product, TagEntity tag, Integer version) {
        this.product = product;
        this.tag = tag;
        this.version = version;
    }

}
