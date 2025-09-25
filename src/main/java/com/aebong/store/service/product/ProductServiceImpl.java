package com.aebong.store.service.product;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.exceptions.ProductApplicationException;
import com.aebong.store.domain.repository.product.ProductDetailRepository;
import com.aebong.store.domain.repository.product.ProductRepository;
import com.aebong.store.service.product.dto.ProductRegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public void registerProduct(ProductRegisterRequest registerRequest) {

        // check bad request
        if (Objects.isNull(registerRequest) || Objects.isNull(registerRequest.getPrice()) || registerRequest.getImageList().isEmpty()) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, "registerInfo must not be null");
        }

        // check if product code exists
        if (validateProductCodeIsExists(registerRequest.getProductCode())) {
            throw new ProductApplicationException(CustomErrorType.IS_EXIST_PRODUCT_CODE,
                    String.format("%s already exists productCode", registerRequest.getProductCode()));
        }

        // check if product name exists
        if (validateProductNameIsExists(registerRequest.getProductName())) {
            throw new ProductApplicationException(CustomErrorType.IS_EXIST_PRODUCT_NAME,
                    String.format("%s already exists productName", registerRequest.getProductName()));
        }

        // check valid price
        if (validateAmountIsInvalid(registerRequest.getPrice().getSalesAmount())) {
            throw new ProductApplicationException(CustomErrorType.INVALID_AMOUNT,
                    String.format("%s this amount is invalid", registerRequest.getPrice().getSalesAmount()));
        } else if (validateAmountIsInvalid(registerRequest.getPrice().getPurchaseAmount())) {
            throw new ProductApplicationException(CustomErrorType.INVALID_AMOUNT,
                    String.format("%s this amount is invalid", registerRequest.getPrice().getPurchaseAmount()));
        } else if (validateAmountIsInvalid(registerRequest.getPrice().getDiscountAmount())) {
            throw new ProductApplicationException(CustomErrorType.INVALID_AMOUNT,
                    String.format("%s this amount is invalid", registerRequest.getPrice().getDiscountAmount()));
        }

        // todo: check valid image format

    }

    private boolean validateProductCodeIsExists(String productCode) {
        if (Objects.isNull(productCode)) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, "productCode is null");
        }
        return productRepository.existsByProductCode(productCode);
    }

    private boolean validateProductNameIsExists(String productName) {
        if (Objects.isNull(productName)) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, "productName is null");
        }
        return productDetailRepository.existsByProductName(productName);
    }

    private boolean validateAmountIsInvalid(BigDecimal amount) {
        if (Objects.isNull(amount)) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, "amount is null");
        }
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

}
