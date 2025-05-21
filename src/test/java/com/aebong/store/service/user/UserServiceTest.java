package com.aebong.store.service.user;

import com.aebong.store.annotation.UnitTest;
import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.domain.entity.Address;
import com.aebong.store.domain.entity.user.UserDetailEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.UserDetailRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import com.aebong.store.service.user.dto.UserRegisterInfo;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired private UserService service;
    @Autowired private UserRepository userRepository;
    @Autowired private UserDetailRepository userDetailRepository;
    @Autowired private EntityManager em;

    @DisplayName("UserService:registerUser 메소드 테스트")
    @Test
    void registerUser_test() {
        // given
        UserRegisterInfo registerInfo = createUserRegisterInfo();

        String validateUserAccount = "aebong@gmail.com";
        String validateEmail = "aebong@gmail.com";

        // when
        this.service.registerUser(registerInfo);

        em.flush();
        em.clear();

        boolean isEqualsUserAccount = this.userRepository.existsByUserAccount(validateUserAccount);
        boolean isEqualsEmail = this.userDetailRepository.existsByEmail(validateEmail);

        // then
        assertThat(isEqualsUserAccount).isTrue();
        assertThat(isEqualsEmail).isTrue();

    }

    private UserRegisterInfo createUserRegisterInfo() {
        return UserRegisterInfo.builder()
                .userType(UserType.REGULAR_MEMBER)
                .userAccount("aebong@gmail.com")
                .userAccountType(UserAccountType.EMAIL)
                .userPassword("nonencodepassword")
                .userStatus(UserStatus.ACTIVATED)
                .requiredPasswordChangeDatetime(LocalDateTime.now().plusDays(90))
                .firstName("애봉")
                .lastName("이")
                .gender(Gender.MALE)
                .email("aebong@gmail.com")
                .address1("대구광역시 수성구 동대구로 86")
                .zipcode("42176")
                .build();
    }

}