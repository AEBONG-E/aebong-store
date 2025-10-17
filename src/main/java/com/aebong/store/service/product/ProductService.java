package com.aebong.store.service.product;

import com.aebong.store.service.product.dto.ProductGetInfo;
import com.aebong.store.service.product.dto.ProductGetRequest;
import com.aebong.store.service.product.dto.ProductRegisterRequest;

import java.util.List;

public interface ProductService {

    /**
     * 상품 등록
     * @param registerRequest 상품 등록 정보(api 통해 request 로 부터 받아올 매개변수)
     */
    void registerProduct(ProductRegisterRequest registerRequest);

    /**
     * 상품 정보 조회
     * @param getRequest
     * @return
     */
    ProductGetInfo getProduct(ProductGetRequest getRequest);

    /**
     * 상품 정보 목록 조회
     * @param getRequest
     * @return
     */
    List<ProductGetInfo> getProducts(ProductGetRequest getRequest);

}
