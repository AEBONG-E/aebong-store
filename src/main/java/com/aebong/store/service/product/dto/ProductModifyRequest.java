package com.aebong.store.service.product.dto;

import com.aebong.store.common.enums.product.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductModifyRequest {

    private ProductType productType;
    private Integer stock;
    private String productName;
    private String productEnglishName;
    private String productShortName;
    private String basicDescription;
    private String detailDescription;
    private String manufacturerCountry;

}
