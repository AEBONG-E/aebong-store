package com.aebong.store.service.product.mapper;

import com.aebong.store.controller.req.ProductPriceRequest;
import com.aebong.store.controller.res.ProductPriceResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ProductPriceMapper {

    private ProductPriceMapper() {
    }

    public static ProductPriceResponse toResponse(ProductPriceRequest priceRequest) {
        if (Objects.isNull(priceRequest)) return null;

        return ProductPriceResponse.builder()
                .priceId(priceRequest.getPriceId())
                .applyStartDate(priceRequest.getApplyStartDate())
                .applyEndDate(priceRequest.getApplyEndDate())
                .salesAmount(priceRequest.getSalesAmount())
                .purchaseAmount(priceRequest.getPurchaseAmount())
                .discountType(priceRequest.getDiscountType())
                .discountAmount(priceRequest.getDiscountAmount())
                .discount(priceRequest.getDiscount())
                .build();
    }

    public static List<ProductPriceResponse> toResponseList(List<ProductPriceRequest> priceRequests) {
        return Optional.ofNullable(priceRequests)
                .orElse(Collections.emptyList())
                .stream()
                .map(ProductPriceMapper::toResponse)
                .toList();
    }

}
