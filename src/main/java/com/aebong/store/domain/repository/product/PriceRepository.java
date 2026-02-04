package com.aebong.store.domain.repository.product;

import com.aebong.store.domain.entity.product.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<PriceEntity, Long> {

}
