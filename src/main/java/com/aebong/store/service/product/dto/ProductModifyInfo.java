package com.aebong.store.service.product.dto;

import com.aebong.store.common.enums.product.ProductType;
import com.aebong.store.controller.req.ProductModifyRequest;
import com.aebong.store.controller.res.ProductImageResponse;
import com.aebong.store.controller.res.ProductPriceResponse;
import com.aebong.store.service.product.mapper.ProductImageMapper;
import com.aebong.store.service.product.mapper.ProductPriceMapper;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class ProductModifyInfo {

    private Long productId;
    private Long productDetailId;
    private String productCode;
    private ProductType productType;
    private String productName;
    private String productEnglishName;
    private String productShortName;
    private String basicDescription;
    private String detailDescription;
    private String manufacturerCountry;
    private LocalDateTime releaseDatetime;
    private ProductPriceResponse price;
    private List<ProductImageResponse> imageList = new ArrayList<>();

    @Builder
    public ProductModifyInfo(Long productId,
                             Long productDetailId,
                             String productCode,
                             ProductType productType,
                             String productName,
                             String productEnglishName,
                             String productShortName,
                             String basicDescription,
                             String detailDescription,
                             String manufacturerCountry,
                             LocalDateTime releaseDatetime,
                             ProductPriceResponse price,
                             List<ProductImageResponse> imageList) {
        this.productId = productId;
        this.productDetailId = productDetailId;
        this.productCode = productCode;
        this.productType = productType;
        this.productName = productName;
        this.productEnglishName = productEnglishName;
        this.productShortName = productShortName;
        this.basicDescription = basicDescription;
        this.detailDescription = detailDescription;
        this.manufacturerCountry = manufacturerCountry;
        this.releaseDatetime = releaseDatetime;
        this.price = price;
        this.imageList = imageList;
    }

    public static ProductModifyInfo to(ProductModifyRequest modifyRequest) {
        if (modifyRequest == null) return null;
        return ProductModifyInfo.builder()
                .productId(modifyRequest.getProductId())
                .productDetailId(modifyRequest.getProductDetailId())
                .productCode(modifyRequest.getProductCode())
                .productType(modifyRequest.getProductType())
                .productName(modifyRequest.getProductName())
                .productEnglishName(modifyRequest.getProductEnglishName())
                .productShortName(modifyRequest.getProductShortName())
                .basicDescription(modifyRequest.getBasicDescription())
                .detailDescription(modifyRequest.getDetailDescription())
                .manufacturerCountry(modifyRequest.getManufacturerCountry())
                .releaseDatetime(modifyRequest.getReleaseDatetime())
                .price(ProductPriceMapper.toResponse(modifyRequest.getPrice()))
                .imageList(ProductImageMapper.toResponseList(modifyRequest.getImageList()))
                .build();
    }

}
