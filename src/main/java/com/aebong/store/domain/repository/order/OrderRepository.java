package com.aebong.store.domain.repository.order;

import com.aebong.store.common.enums.order.OrderStatus;
import com.aebong.store.domain.entity.order.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    @Query("SELECT o FROM OrderEntity o WHERE o.user.userAccount = :userAccount ORDER BY o.createdAt DESC")
    Page<OrderEntity> findByUserAccount(@Param("userAccount") String userAccount, Pageable pageable);

    Page<OrderEntity> findByStatus(OrderStatus status, Pageable pageable);
}
