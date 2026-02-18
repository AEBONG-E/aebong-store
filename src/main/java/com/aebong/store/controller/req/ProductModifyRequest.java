package com.aebong.store.controller.req;

import com.aebong.store.common.enums.product.ProductType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductModifyRequest {

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
    private ProductPriceRequest price;
    private List<ProductImageRequest> imageList = new ArrayList<>();

    @Builder
    public ProductModifyRequest(Long productId,
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
                                ProductPriceRequest price,
                                List<ProductImageRequest> imageList) {
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

}
