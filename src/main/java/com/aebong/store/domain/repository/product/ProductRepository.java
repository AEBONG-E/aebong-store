package com.aebong.store.domain.repository.product;

import com.aebong.store.domain.entity.product.ProductEntity;
import com.aebong.store.domain.repository.custom.ProductCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductCustomRepository {

    boolean existsByProductCode(String productCode);

}
