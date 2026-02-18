package com.aebong.store.service.product.dto;

import com.aebong.store.common.enums.product.ProductType;
import com.aebong.store.controller.res.ProductImageResponse;
import com.aebong.store.controller.res.ProductPriceResponse;
import com.aebong.store.domain.entity.product.ImageEntity;
import com.aebong.store.domain.entity.product.PriceEntity;
import com.aebong.store.domain.entity.product.ProductEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
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
    private List<ProductPriceResponse> priceList = new ArrayList<>();
    private List<ProductImageResponse> imageList = new ArrayList<>();

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
                          List<ProductPriceResponse> priceList,
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
        this.priceList= priceList;
        this.imageList = imageList;
    }

    public static ProductGetInfo to(ProductEntity product) {
        if (product == null) return null;

        return ProductGetInfo.builder()
                .productId(product.getId())
                .productDetailId(product.getProductDetail().getId())
                .productCode(product.getProductCode())
                .productType(product.getProductType())
                .productName(product.getProductDetail().getProductName())
                .productEnglishName(product.getProductDetail().getProductEnglishName())
                .productShortName(product.getProductDetail().getProductShortName())
                .basicDescription(product.getProductDetail().getBasicDescription())
                .detailDescription(product.getProductDetail().getDetailDescription())
                .manufacturerCountry(product.getProductDetail().getManufacturerCountry())
                .releaseDatetime(product.getProductDetail().getReleaseDatetime())
                .priceList(toPriceList(product))
                .imageList(toImageList(product))
                .build();
    }

    private static List<ProductPriceResponse> toPriceList(ProductEntity product) {
        if (product == null || product.getPrices().isEmpty()) return null;

        List<ProductPriceResponse> priceList = new ArrayList<>();

        for (PriceEntity price : product.getPrices()) {
            if (price == null) return null;
            ProductPriceResponse priceInfo = ProductPriceResponse.builder()
                    .priceId(price.getId())
                    .applyStartDate(price.getApplyStartDate())
                    .applyEndDate(price.getApplyEndDate())
                    .salesAmount(price.getSalesAmount())
                    .purchaseAmount(price.getPurchaseAmount())
                    .discountType(price.getDiscountType())
                    .discountAmount(price.getDiscountAmount())
                    .discount(price.getDiscount())
                    .build();
            priceList.add(priceInfo);
        }
        return priceList;
    }

    private static List<ProductImageResponse> toImageList(ProductEntity product) {
        if (product == null || product.getImages().isEmpty()) return null;

        List<ProductImageResponse> imageList = new ArrayList<>();

        for (ImageEntity image : product.getImages()) {
            if (image == null) return null;
            ProductImageResponse imageInfo = ProductImageResponse.builder()
                    .imageId(image.getId())
                    .adminImageFileName(image.getAdminImageFileName())
                    .originalImageFileName(image.getOriginalImageFileName())
                    .imageFileName(image.getImageFileName())
                    .imageFileUrl(image.getImageFileUrl())
                    .imageType(image.getImageType())
                    .contentType(image.getContentType())
                    .width(image.getWidth())
                    .height(image.getHeight())
                    .fileSize(image.getFileSize())
                    .build();
            imageList.add(imageInfo);
        }
        return imageList;
    }

}
