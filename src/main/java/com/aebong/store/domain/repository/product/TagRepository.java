package com.aebong.store.domain.repository.product;

import com.aebong.store.domain.entity.product.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

}
