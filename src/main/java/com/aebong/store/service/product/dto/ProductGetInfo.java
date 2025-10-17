package com.aebong.store.service.product.dto;

import com.aebong.store.common.enums.product.ContentType;
import com.aebong.store.common.enums.product.DiscountType;
import com.aebong.store.common.enums.product.ImageType;
import com.aebong.store.common.enums.product.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductGetInfo {

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
    private List<ProductGetInfo.PriceListGetInfo> priceList = new ArrayList<>();
    private List<ProductGetInfo.ImageListGetInfo> imageList = new ArrayList<>();

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PriceListGetInfo {
        private Long priceId;
        private LocalDate applyStartDate;
        private LocalDate applyEndDate;
        private BigDecimal salesAmount;
        private BigDecimal purchaseAmount;
        private DiscountType discountType;
        private BigDecimal discountAmount;
        private Long discount;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageListGetInfo {
        private Long imageId;
        private String adminImageFileName;
        private String originalImageFileName;
        private String imageFileName;
        private String imageFileUrl;
        private ImageType imageType;
        private ContentType contentType;
        private Integer width;
        private Integer height;
        private Integer fileSize;
    }

    @Builder
    public ProductGetInfo(Long productId,
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
                          List<ProductGetInfo.PriceListGetInfo> priceList,
                          List<ProductGetInfo.ImageListGetInfo> imageList) {
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
        this.priceList= priceList;
        this.imageList = imageList;
    }

}
