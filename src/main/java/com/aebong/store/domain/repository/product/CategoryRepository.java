package com.aebong.store.domain.repository.product;

import com.aebong.store.domain.entity.product.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
