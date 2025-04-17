package com.aebong.store.domain.repository.user;

import com.aebong.store.domain.entity.user.UserDeliveryAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeliveryAddressRepository extends JpaRepository<UserDeliveryAddressEntity, Long> {

}
