package com.aebong.store.domain.repository.user;

import com.aebong.store.domain.entity.user.UserDetailEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetailEntity, Long> {

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByNickName(String nickName);

    boolean existsByEmail(String email);

    Optional<UserDetailEntity> findByUser(UserEntity user);

}
