package com.aebong.store.service.product.mapper;

import com.aebong.store.controller.req.ProductImageRequest;
import com.aebong.store.controller.res.ProductImageResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ProductImageMapper {

    private ProductImageMapper() {
    }

    public static ProductImageResponse toResponse(ProductImageRequest imageRequest) {
        if (Objects.isNull(imageRequest)) return null;

        return ProductImageResponse.builder()
                .imageId(imageRequest.getImageId())
                .adminImageFileName(imageRequest.getAdminImageFileName())
                .originalImageFileName(imageRequest.getOriginalImageFileName())
                .imageFileName(imageRequest.getImageFileName())
                .imageFileUrl(imageRequest.getImageFileUrl())
                .imageType(imageRequest.getImageType())
                .contentType(imageRequest.getContentType())
                .width(imageRequest.getWidth())
                .height(imageRequest.getHeight())
                .fileSize(imageRequest.getFileSize())
                .build();
    }

    public static List<ProductImageResponse> toResponseList(List<ProductImageRequest> imageRequests) {
        return Optional.ofNullable(imageRequests)
                .orElse(Collections.emptyList())
                .stream()
                .map(ProductImageMapper::toResponse)
                .toList();
    }

}
