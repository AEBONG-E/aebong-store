package com.aebong.store.service.product;

import com.aebong.store.service.product.dto.ProductGetInfo;
import com.aebong.store.controller.req.ProductRegisterRequest;
import com.aebong.store.controller.req.ProductModifyRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    /**
     * 상품 등록
     * @param registerRequest 상품 등록 정보(api 통해 request 로 부터 받아올 매개변수)
     */
    void registerProduct(ProductRegisterRequest registerRequest);

    /**
     * 상품 정보 조회
     * @param productId
     * @return ProductGetInfo
     */
    ProductGetInfo getProduct(Long productId);

    /**
     * 상품 정보 목록 조회
     * @param pageable
     * @return Page<ProductGetInfo>
     */
    Page<ProductGetInfo> getProducts(Pageable pageable);

    /**
     * 상품 수정
     * @param modifyRequest 상품 수정 정보(api 통해 request 로 부터 받아올 매개변수)
     */
    void modifyProduct(Long productId, ProductModifyRequest modifyRequest);

}
