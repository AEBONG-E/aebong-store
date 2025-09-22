package com.aebong.store.service.product.dto;

import com.aebong.store.common.enums.product.ContentType;
import com.aebong.store.common.enums.product.ImageType;
import com.aebong.store.common.enums.product.ProductType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductRegisterRequest {

    private String productCode;
    private String productName;
    private Long amount;
    private String brand;
    private ProductType productType;
    private List<ImageRegisterRequest> imageList = new ArrayList<>();

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
                                  String productName,
                                  Long amount,
                                  String brand,
                                  ProductType productType,
                                  List<ImageRegisterRequest> imageList) {
        this.productCode = productCode;
        this.productName = productName;
        this.amount = amount;
        this.brand = brand;
        this.productType = productType;
        this.imageList = imageList;
    }

}
