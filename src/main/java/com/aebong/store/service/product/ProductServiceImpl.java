package com.aebong.store.service.product;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.exceptions.ProductApplicationException;
import com.aebong.store.service.product.dto.ProductRegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public void registerProduct(ProductRegisterRequest registerRequest) {

        if (Objects.isNull(registerRequest)) {
            throw new ProductApplicationException(CustomErrorType.INTERNAL_SERVER_ERROR, "registerInfo must not be null");
        }

        // check if product name exists

        // check if product code exists

        // check valid amount

        // check valid image format

    }

}
