package com.aebong.store.service.product.dto;

import com.aebong.store.common.enums.product.ContentType;
import com.aebong.store.common.enums.product.DiscountType;
import com.aebong.store.common.enums.product.ImageType;
import com.aebong.store.common.enums.product.ProductType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductRegisterRequest {

    private String productCode;
    private ProductType productType;
    private String productName;
    private String productEnglishName;
    private String productShortName;
    private String basicDescription;
    private String detailDescription;
    private String manufacturerCountry;
    private LocalDateTime releaseDatetime;
    private PriceRegisterRequest price;
    private List<ImageRegisterRequest> imageList = new ArrayList<>();

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PriceRegisterRequest {
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
    public static class ImageRegisterRequest {
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
    public ProductRegisterRequest(String productCode,
                                  ProductType productType,
                                  String productName,
                                  String productEnglishName,
                                  String productShortName,
                                  String basicDescription,
                                  String detailDescription,
                                  String manufacturerCountry,
                                  LocalDateTime releaseDatetime,
                                  PriceRegisterRequest price,
                                  List<ImageRegisterRequest> imageList) {
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

}
