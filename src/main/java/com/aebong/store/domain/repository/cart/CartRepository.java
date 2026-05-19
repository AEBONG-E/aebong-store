package com.aebong.store.domain.repository.cart;

import com.aebong.store.domain.entity.cart.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    @Query("SELECT c FROM CartEntity c JOIN FETCH c.items ci JOIN FETCH ci.product WHERE c.user.userAccount = :userAccount")
    Optional<CartEntity> findByUserAccountWithItems(@Param("userAccount") String userAccount);

    Optional<CartEntity> findByUserUserAccount(String userAccount);
}
