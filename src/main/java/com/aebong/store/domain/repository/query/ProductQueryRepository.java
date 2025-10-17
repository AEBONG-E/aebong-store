package com.aebong.store.domain.repository.query;

import com.aebong.store.service.product.dto.ProductGetInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.aebong.store.domain.entity.product.QProductDetailEntity.productDetailEntity;
import static com.aebong.store.domain.entity.product.QProductEntity.productEntity;

@RequiredArgsConstructor
@Repository
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    Optional<ProductGetInfo> findById(Long productId) {

        return queryFactory
                .select(Projections.fields(ProductGetInfo.class,
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
                                           productDetailEntity.releaseDatetime

                ))
                .from(productEntity)
                .join(productDetailEntity).on(productDetailEntity.product.eq(productEntity))
                .where(productEntity.id.eq(productId))
                .orderBy(productDetailEntity.releaseDatetime.desc())
                .stream().findFirst();

    }

}
