package com.aebong.store.domain.repository.user;

import com.aebong.store.domain.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUserAccount(String userAccount);

}
