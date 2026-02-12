package com.aebong.store.service.product.validator;

import com.aebong.store.controller.req.ProductModifyRequest;
import com.aebong.store.controller.req.ProductRegisterRequest;

public interface ProductValidator {

    void validateForRegister(ProductRegisterRequest registerRequest);

    void validateForModify(ProductModifyRequest modifyRequest);

}
