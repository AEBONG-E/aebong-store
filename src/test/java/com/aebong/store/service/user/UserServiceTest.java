package com.aebong.store.service.user;

import com.aebong.store.annotation.UnitTest;
import com.aebong.store.domain.repository.user.UserDetailRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

@UnitTest
class UserServiceTest {
    
    @Autowired private UserRepository userRepository;
    @Autowired private UserDetailRepository userDetailRepository;

    @DisplayName("유효한 사용자 정보가 주어지면 정상적으로 사용자 정보가 저장되어야 한다.")
    @Test
    void givenValidUserRegisterInfo_whenSaving_thenUserIsPersisted() {
        // given

        // when

        // then

    }

}