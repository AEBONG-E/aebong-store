package com.aebong.store.domain.repository.user;

import com.aebong.store.domain.entity.user.UserSocialAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSocialAccountRepository extends JpaRepository<UserSocialAccountEntity, Long> {

}
