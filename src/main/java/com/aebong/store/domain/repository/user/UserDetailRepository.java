package com.aebong.store.domain.repository.user;

import com.aebong.store.domain.entity.user.UserDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetailEntity, Long> {

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByNickName(String nickName);

    boolean existsByEmail(String email);

}
