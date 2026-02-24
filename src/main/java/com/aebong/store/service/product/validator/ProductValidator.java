package com.aebong.store.service.product.validator;

import com.aebong.store.controller.product.req.ProductModifyRequest;
import com.aebong.store.controller.product.req.ProductRegisterRequest;

public interface ProductValidator {

    void validateForRegister(ProductRegisterRequest registerRequest);

    void validateForModify(ProductModifyRequest modifyRequest);

}
