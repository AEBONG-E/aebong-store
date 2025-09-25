package com.aebong.store.service.product.dto;

import com.aebong.store.common.enums.product.ContentType;
import com.aebong.store.common.enums.product.DiscountType;
import com.aebong.store.common.enums.product.ImageType;
import com.aebong.store.common.enums.product.ProductType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
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
    private String adminImageFileName;
    private String originalImageFileName;
    private String imageFileName;
    private String imageFileUrl;
    private ImageType imageType;
    private ContentType contentType;
    private Integer width;
    private Integer height;
    private Integer fileSize;

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
                               String adminImageFileName,
                               String originalImageFileName,
                               String imageFileName,
                               String imageFileUrl,
                               ImageType imageType,
                               ContentType contentType,
                               Integer width,
                               Integer height,
                               Integer fileSize) {
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
        this.adminImageFileName = adminImageFileName;
        this.originalImageFileName = originalImageFileName;
        this.imageFileName = imageFileName;
        this.imageFileUrl = imageFileUrl;
        this.imageType = imageType;
        this.contentType = contentType;
        this.width = width;
        this.height = height;
        this.fileSize = fileSize;
    }

}
