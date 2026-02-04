package com.aebong.store.domain.repository.product;

import com.aebong.store.domain.entity.product.ProductDetailEntity;
import com.aebong.store.domain.entity.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetailEntity, Long> {

    boolean existsByProductName(String productName);

}
