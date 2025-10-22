package com.aebong.store.domain.repository.custom;

import com.aebong.store.service.product.dto.ProductGetInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.aebong.store.domain.entity.product.QImageEntity.imageEntity;
import static com.aebong.store.domain.entity.product.QPriceEntity.priceEntity;
import static com.aebong.store.domain.entity.product.QProductDetailEntity.productDetailEntity;
import static com.aebong.store.domain.entity.product.QProductEntity.productEntity;

@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<ProductGetInfo> findByproductId(Long productId) {

        ProductGetInfo productGetInfo = queryFactory
                .select(Projections.constructor(ProductGetInfo.class,
                                                productEntity.id.as("productId"),
                                                productDetailEntity.id.as("productDetailId"),
                                                productEntity.productCode,
                                                productEntity.productType,
                                                productDetailEntity.productName,
                                                productDetailEntity.productEnglishName,
                                                productDetailEntity.productShortName,
                                                productDetailEntity.basicDescription,
                                                productDetailEntity.detailDescription,
                                                productDetailEntity.manufacturerCountry,
                                                productDetailEntity.releaseDatetime,
                                                Projections.list(
                                                        priceEntity.id.as("priceId"),
                                                        priceEntity.applyStartDate,
                                                        priceEntity.applyEndDate,
                                                        priceEntity.salesAmount,
                                                        priceEntity.purchaseAmount,
                                                        priceEntity.discountType,
                                                        priceEntity.discountAmount,
                                                        priceEntity.discount
                                                ),
                                                Projections.list(
                                                        imageEntity.id.as("imageId"),
                                                        imageEntity.adminImageFileName,
                                                        imageEntity.originalImageFileName,
                                                        imageEntity.imageFileName,
                                                        imageEntity.imageFileUrl,
                                                        imageEntity.imageType,
                                                        imageEntity.contentType,
                                                        imageEntity.width,
                                                        imageEntity.height,
                                                        imageEntity.fileSize
                                                )

                ))
                .from(productEntity)
                .join(productDetailEntity).on(productDetailEntity.product.eq(productEntity))
                .join(priceEntity).on(priceEntity.product.eq(productEntity))
                .join(imageEntity).on(imageEntity.product.eq(productEntity))
                .where(productEntity.id.eq(productId))
                .orderBy(productDetailEntity.releaseDatetime.desc())
                .fetchOne();

        return Optional.ofNullable(productGetInfo);

    }

    public Page<ProductGetInfo> findAllProducts(Pageable pageable) {

        List<ProductGetInfo> query = queryFactory
                .select(Projections.constructor(ProductGetInfo.class,
                                                productEntity.id.as("productId"),
                                                productDetailEntity.id.as("productDetailId"),
                                                productEntity.productCode,
                                                productEntity.productType,
                                                productDetailEntity.productName,
                                                productDetailEntity.productEnglishName,
                                                productDetailEntity.productShortName,
                                                productDetailEntity.basicDescription,
                                                productDetailEntity.detailDescription,
                                                productDetailEntity.manufacturerCountry,
                                                productDetailEntity.releaseDatetime,
                                                Projections.list(
                                                        priceEntity.id.as("priceId"),
                                                        priceEntity.applyStartDate,
                                                        priceEntity.applyEndDate,
                                                        priceEntity.salesAmount,
                                                        priceEntity.purchaseAmount,
                                                        priceEntity.discountType,
                                                        priceEntity.discountAmount,
                                                        priceEntity.discount
                                                ),
                                                Projections.list(
                                                        imageEntity.id.as("imageId"),
                                                        imageEntity.adminImageFileName,
                                                        imageEntity.originalImageFileName,
                                                        imageEntity.imageFileName,
                                                        imageEntity.imageFileUrl,
                                                        imageEntity.imageType,
                                                        imageEntity.contentType,
                                                        imageEntity.width,
                                                        imageEntity.height,
                                                        imageEntity.fileSize
                                                )

                ))
                .from(productEntity)
                .join(productDetailEntity).on(productDetailEntity.product.eq(productEntity))
                .join(priceEntity).on(priceEntity.product.eq(productEntity))
                .join(imageEntity).on(imageEntity.product.eq(productEntity))
                .orderBy(productDetailEntity.releaseDatetime.desc())
                .fetch();

        return new PageImpl<>(query, pageable, query.size());

    }

}
