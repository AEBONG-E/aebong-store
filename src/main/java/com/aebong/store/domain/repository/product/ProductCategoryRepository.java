package com.aebong.store.domain.repository.product;

import com.aebong.store.domain.entity.product.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {

}
