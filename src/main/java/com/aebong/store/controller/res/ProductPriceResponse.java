package com.aebong.store.controller.res;

import com.aebong.store.common.enums.product.DiscountType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPriceResponse {

    private Long priceId;
    private LocalDate applyStartDate;
    private LocalDate applyEndDate;
    private BigDecimal salesAmount;
    private BigDecimal purchaseAmount;
    private DiscountType discountType;
    private BigDecimal discountAmount;
    private Long discount;

}
