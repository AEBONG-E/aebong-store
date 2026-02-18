package com.aebong.store.service.product.dto;

import com.aebong.store.common.enums.product.DiscountType;
import com.aebong.store.common.enums.product.ProductType;
import com.aebong.store.controller.req.ProductRegisterRequest;
import com.aebong.store.controller.res.ProductImageResponse;
import com.aebong.store.service.product.mapper.ProductImageMapper;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class ProductRegisterInfo {

    private String productCode;
    private ProductType productType;
    private String productName;
    private String productEnglishName;
    private String productShortName;
    private String basicDescription;
    private String detailDescription;
    private String manufacturerCountry;
    private LocalDateTime releaseDatetime;
    private LocalDate applyStartDate;
    private LocalDate applyEndDate;
    private BigDecimal salesAmount;
    private BigDecimal purchaseAmount;
    private DiscountType discountType;
    private BigDecimal discountAmount;
    private Long discount;
    private List<ProductImageResponse> imageList = new ArrayList<>();

    @Builder
    public ProductRegisterInfo(String productCode,
                               ProductType productType,
                               String productName,
                               String productEnglishName,
                               String productShortName,
                               String basicDescription,
                               String detailDescription,
                               String manufacturerCountry,
                               LocalDateTime releaseDatetime,
                               LocalDate applyStartDate,
                               LocalDate applyEndDate,
                               BigDecimal salesAmount,
                               BigDecimal purchaseAmount,
                               DiscountType discountType,
                               BigDecimal discountAmount,
                               Long discount,
                               List<ProductImageResponse> imageList) {
        this.productCode = productCode;
        this.productType = productType;
        this.productName = productName;
        this.productEnglishName = productEnglishName;
        this.productShortName = productShortName;
        this.basicDescription = basicDescription;
        this.detailDescription = detailDescription;
        this.manufacturerCountry = manufacturerCountry;
        this.releaseDatetime = releaseDatetime;
        this.applyStartDate = applyStartDate;
        this.applyEndDate = applyEndDate;
        this.salesAmount = salesAmount;
        this.purchaseAmount = purchaseAmount;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.discount = discount;
        this.imageList = imageList;
    }

    public static ProductRegisterInfo to(ProductRegisterRequest registerRequest) {
        if (registerRequest == null) return null;
        return ProductRegisterInfo.builder()
                .productCode(registerRequest.getProductCode())
                .productType(registerRequest.getProductType())
                .productName(registerRequest.getProductName())
                .productEnglishName(registerRequest.getProductEnglishName())
                .productShortName(registerRequest.getProductShortName())
                .basicDescription(registerRequest.getBasicDescription())
                .detailDescription(registerRequest.getDetailDescription())
                .manufacturerCountry(registerRequest.getManufacturerCountry())
                .releaseDatetime(registerRequest.getReleaseDatetime())
                .applyStartDate(registerRequest.getPrice().getApplyStartDate())
                .applyEndDate(registerRequest.getPrice().getApplyEndDate())
                .salesAmount(registerRequest.getPrice().getSalesAmount())
                .purchaseAmount(registerRequest.getPrice().getPurchaseAmount())
                .discountType(registerRequest.getPrice().getDiscountType())
                .discountAmount(registerRequest.getPrice().getDiscountAmount())
                .discount(registerRequest.getPrice().getDiscount())
                .imageList(ProductImageMapper.toResponseList(registerRequest.getImageList()))
                .build();
    }

}
