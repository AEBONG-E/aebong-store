package com.aebong.store.domain.repository.user;

import com.aebong.store.domain.entity.user.UserInformationChangeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInformationChangeHistoryRepository extends JpaRepository<UserInformationChangeHistoryEntity, Long> {

}
