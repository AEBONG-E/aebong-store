package com.aebong.store.domain.repository.product;

import com.aebong.store.domain.entity.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}
