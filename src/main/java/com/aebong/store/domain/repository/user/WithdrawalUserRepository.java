package com.aebong.store.domain.repository.user;

import com.aebong.store.domain.entity.user.WithdrawalUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalUserRepository extends JpaRepository<WithdrawalUserEntity, Long> {

}
