package com.aebong.store.service.product.validator;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.exceptions.ProductApplicationException;
import com.aebong.store.controller.product.req.ProductImageRequest;
import com.aebong.store.controller.product.req.ProductModifyRequest;
import com.aebong.store.controller.product.req.ProductRegisterRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class DefaultProductValidator implements ProductValidator {

    private static final int PRODUCT_CODE_MAX_LENGTH = 20;
    private static final int PRODUCT_NAME_MAX_LENGTH = 150;
    private static final int PRODUCT_ENGLISH_NAME_MAX_LENGTH = 200;
    private static final int PRODUCT_SHORT_NAME_MAX_LENGTH = 100;
    private static final int BASIC_DESCRIPTION_MAX_LENGTH = 300;
    private static final int MANUFACTURER_COUNTRY_MAX_LENGTH = 20;
    private static final int IMAGE_FILE_NAME_MAX_LENGTH = 1000;

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

        if (Objects.isNull(modifyRequest) || Objects.isNull(modifyRequest.getPrice())
                || Objects.isNull(modifyRequest.getImageList()) || modifyRequest.getImageList().isEmpty()) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, "modifyRequest must not be null");
        }

        validateRequiredString(modifyRequest.getProductCode(), "productCode is null");
        validateRequiredString(modifyRequest.getProductName(), "productName is null");

        validateNotBlank(modifyRequest.getProductCode(), "productCode is blank");
        validateNotBlank(modifyRequest.getProductName(), "productName is blank");
        validateMaxLengthForModify(modifyRequest);

        if (Objects.isNull(modifyRequest.getPrice().getPriceId())) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, "priceId is null");
        }

        validatePriceAmount(modifyRequest.getPrice().getSalesAmount());
        validatePriceAmount(modifyRequest.getPrice().getPurchaseAmount());
        validatePriceAmount(modifyRequest.getPrice().getDiscountAmount());
    }

    private void validateRequiredString(String value, String message) {
        if (Objects.isNull(value)) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, message);
        }
    }

    private void validateNotBlank(String value, String message) {
        if (value.isBlank()) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, message);
        }
    }

    /**
     * 컬럼 길이는 엔티티 / DDL 기준이다. TEXT(@Lob) 컬럼인 detailDescription, imageFileUrl 은 검사하지 않는다.
     */
    private void validateMaxLengthForModify(ProductModifyRequest modifyRequest) {
        validateMaxLength(modifyRequest.getProductCode(), PRODUCT_CODE_MAX_LENGTH, "productCode");
        validateMaxLength(modifyRequest.getProductName(), PRODUCT_NAME_MAX_LENGTH, "productName");
        validateMaxLength(modifyRequest.getProductEnglishName(), PRODUCT_ENGLISH_NAME_MAX_LENGTH, "productEnglishName");
        validateMaxLength(modifyRequest.getProductShortName(), PRODUCT_SHORT_NAME_MAX_LENGTH, "productShortName");
        validateMaxLength(modifyRequest.getBasicDescription(), BASIC_DESCRIPTION_MAX_LENGTH, "basicDescription");
        validateMaxLength(modifyRequest.getManufacturerCountry(), MANUFACTURER_COUNTRY_MAX_LENGTH, "manufacturerCountry");

        for (ProductImageRequest image : modifyRequest.getImageList()) {
            if (Objects.isNull(image)) continue;
            validateMaxLength(image.getAdminImageFileName(), IMAGE_FILE_NAME_MAX_LENGTH, "adminImageFileName");
            validateMaxLength(image.getOriginalImageFileName(), IMAGE_FILE_NAME_MAX_LENGTH, "originalImageFileName");
            validateMaxLength(image.getImageFileName(), IMAGE_FILE_NAME_MAX_LENGTH, "imageFileName");
        }
    }

    private void validateMaxLength(String value, int maxLength, String fieldName) {
        if (Objects.nonNull(value) && value.codePointCount(0, value.length()) > maxLength) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST,
                    String.format("%s must be at most %d characters", fieldName, maxLength));
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
