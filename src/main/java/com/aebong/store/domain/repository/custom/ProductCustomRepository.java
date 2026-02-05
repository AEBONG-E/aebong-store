package com.aebong.store.domain.repository.custom;

import com.aebong.store.domain.entity.product.ProductEntity;
import com.aebong.store.service.product.dto.ProductGetInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductCustomRepository {

    Optional<ProductEntity> findByProductId(Long productId);

    Page<ProductGetInfo> findAllProducts(Pageable pageable);

}
