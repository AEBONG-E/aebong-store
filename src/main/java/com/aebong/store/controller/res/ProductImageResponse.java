package com.aebong.store.controller.res;

import com.aebong.store.common.enums.product.ContentType;
import com.aebong.store.common.enums.product.ImageType;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImageResponse {

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
