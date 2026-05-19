package com.aebong.store.domain.repository.user;

import com.aebong.store.domain.entity.user.UserDeliveryAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDeliveryAddressRepository extends JpaRepository<UserDeliveryAddressEntity, Long> {

    List<UserDeliveryAddressEntity> findAllByUser_UserAccountOrderByIdDesc(String userAccount);

    Optional<UserDeliveryAddressEntity> findByIdAndUser_UserAccount(Long id, String userAccount);

    Optional<UserDeliveryAddressEntity> findByUser_UserAccountAndIsDefaultTrue(String userAccount);

}
