package com.aebong.store.service.product.validator;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.exceptions.ProductApplicationException;
import com.aebong.store.controller.req.ProductModifyRequest;
import com.aebong.store.controller.req.ProductRegisterRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class DefaultProductValidator implements ProductValidator {

    @Override
    public void validateForRegister(ProductRegisterRequest registerRequest) {

        if (Objects.isNull(registerRequest) || Objects.isNull(registerRequest.getPrice()) || registerRequest.getImageList().isEmpty()) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, "registerInfo must not be null");
        }

        validateRequiredString(registerRequest.getProductCode(), "productCode is null");
        validateRequiredString(registerRequest.getProductName(), "productName is null");

        validatePriceAmount(registerRequest.getPrice().getSalesAmount());
        validatePriceAmount(registerRequest.getPrice().getPurchaseAmount());
        validatePriceAmount(registerRequest.getPrice().getDiscountAmount());
    }

    @Override
    public void validateForModify(ProductModifyRequest modifyRequest) {

        if (Objects.isNull(modifyRequest) || Objects.isNull(modifyRequest.getPrice()) || modifyRequest.getImageList().isEmpty()) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, "modifyRequest must not be null");
        }

        validateRequiredString(modifyRequest.getProductCode(), "productCode is null");
        validateRequiredString(modifyRequest.getProductName(), "productName is null");

        validatePriceAmount(modifyRequest.getPrice().getSalesAmount());
        validatePriceAmount(modifyRequest.getPrice().getPurchaseAmount());
        validatePriceAmount(modifyRequest.getPrice().getDiscountAmount());
    }

    private void validateRequiredString(String value, String message) {
        if (Objects.isNull(value)) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, message);
        }
    }

    private void validatePriceAmount(BigDecimal amount) {
        if (Objects.isNull(amount)) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, "amount is null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductApplicationException(CustomErrorType.INVALID_AMOUNT,
                    String.format("%s this amount is invalid", amount));
        }
    }

}
