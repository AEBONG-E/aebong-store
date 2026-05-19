package com.aebong.store.service.product;

import com.aebong.store.service.product.dto.ProductGetInfo;
import com.aebong.store.service.product.dto.ProductModifyRequest;
import com.aebong.store.service.product.dto.ProductRegisterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    void registerProduct(ProductRegisterRequest registerRequest);

    ProductGetInfo getProduct(Long productId);

    Page<ProductGetInfo> getProducts(Pageable pageable);

    void modifyProduct(Long productId, ProductModifyRequest modifyRequest);

    void deleteProduct(Long productId);

}
