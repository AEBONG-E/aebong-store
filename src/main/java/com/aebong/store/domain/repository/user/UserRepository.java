package com.aebong.store.domain.repository.user;

import com.aebong.store.domain.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUserAccount(String userAccount);

    Optional<UserEntity> findByUserAccount(String userAccount);

}
