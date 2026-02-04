package com.aebong.store.service.product;

import com.aebong.store.service.product.dto.ProductRegisterRequest;

public interface ProductService {

    /**
     * 상품 등록
     * @param registerRequest 상품 등록 정보(api 통해 request 로 부터 받아올 매개변수)
     */
    void registerProduct(ProductRegisterRequest registerRequest);

}
