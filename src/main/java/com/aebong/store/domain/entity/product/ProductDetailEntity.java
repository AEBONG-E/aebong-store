package com.aebong.store.domain.entity.product;

import com.aebong.store.domain.entity.BaseEntity;
import com.aebong.store.service.product.dto.ProductRegisterInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_detail")
@Entity
public class ProductDetailEntity extends BaseEntity {

    @Comment("상품상세순번 PK")
    @Column(name = "product_detail_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("상품순번")
    @JoinColumn(name = "product_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductEntity product;

    @Comment("상품명")
    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Comment("상품 영문 명")
    @Column(name = "product_english_name", length = 200)
    private String productEnglishName;

    @Comment("상품 짧은 이름")
    @Column(name = "product_short_name", length = 100)
    private String productShortName;

    @Comment("상품 기본 설명 (300자 이내)")
    @Column(name = "basic_description", length = 300)
    private String basicDescription;

    @Comment("상품 상세설명")
    @Column(name = "detail_description", columnDefinition = "TEXT")
    private String detailDescription;

    @Comment("제조국")
    @Column(name = "manufacturer_country", length = 20)
    private String manufacturerCountry;

    @Comment("출시 일시")
    @Column(name = "release_datetime", nullable = false)
    private LocalDateTime releaseDatetime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetailEntity that = (ProductDetailEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Builder
    public ProductDetailEntity(ProductEntity product,
                               String productName,
                               String productEnglishName,
                               String productShortName,
                               String basicDescription,
                               String detailDescription,
                               String manufacturerCountry,
                               LocalDateTime releaseDatetime) {
        this.product = product;
        this.productName = productName;
        this.productEnglishName = productEnglishName;
        this.productShortName = productShortName;
        this.basicDescription = basicDescription;
        this.detailDescription = detailDescription;
        this.manufacturerCountry = manufacturerCountry;
        this.releaseDatetime = releaseDatetime;
    }

    public static ProductDetailEntity create(ProductEntity product, ProductRegisterInfo registerInfo) {
        if (Objects.isNull(product)|| Objects.isNull(registerInfo)) return null;
        return ProductDetailEntity.builder()
                .product(product)
                .productName(registerInfo.getProductName())
                .productEnglishName(registerInfo.getProductEnglishName())
                .productShortName(registerInfo.getProductShortName())
                .basicDescription(registerInfo.getBasicDescription())
                .detailDescription(registerInfo.getDetailDescription())
                .manufacturerCountry(registerInfo.getManufacturerCountry())
                .releaseDatetime(registerInfo.getReleaseDatetime())
                .build();
    }

}
