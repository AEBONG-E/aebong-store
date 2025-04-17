package com.aebong.store.domain.repository.product;

import com.aebong.store.domain.entity.product.ProductTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTagRepository extends JpaRepository<ProductTagEntity, Long> {

}
