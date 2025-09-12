package com.aebong.store.service.user;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.common.exceptions.UserApplicationException;
import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.domain.entity.user.UserDetailEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.UserDetailRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import com.aebong.store.service.user.dto.UserRegisterInfo;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired private EntityManager em;
    @Autowired private UserService service;
    @MockBean private UserRepository userRepository;
    @MockBean private UserDetailRepository userDetailRepository;

    @Test
    void 회원등록_정상_케이스() {
        // given
        UserRegisterInfo registerInfo = UserRegisterInfo.to(createUserRegisterInfo());
        UserEntity userEntity = registerInfo.toUserEntity();
        UserDetailEntity userDetailEntity = registerInfo.toUserDetailEntity(userEntity);

        String validateUserAccount = "aebong@gmail.com";
        String validateEmail = "aebong@gmail.com";

        // when & then
        when(userRepository.existsByUserAccount(validateUserAccount)).thenReturn(false);
        when(userDetailRepository.existsByEmail(validateEmail)).thenReturn(false);
        when(userRepository.saveAndFlush(any())).thenReturn(mock(UserEntity.class));
        when(userDetailRepository.saveAndFlush(any())).thenReturn(mock(UserDetailEntity.class));

        assertThatCode(() -> service.registerUser(createUserRegisterInfo())).doesNotThrowAnyException();

    }

    @Test
    void 회원등록_실패_중복된_userAccount가_들어온_케이스() {
        // given
        UserRegisterInfo registerInfo = UserRegisterInfo.to(createUserRegisterInfo());
        UserEntity userEntity = registerInfo.toUserEntity();
        UserDetailEntity userDetailEntity = registerInfo.toUserDetailEntity(userEntity);

        String validateUserAccount = "aebong@gmail.com";
        String validateEmail = "aebong@gmail.com";

        // when & then
        when(userRepository.existsByUserAccount(validateUserAccount)).thenReturn(true);
        when(userDetailRepository.existsByEmail(validateEmail)).thenReturn(true);
        when(userRepository.saveAndFlush(any())).thenReturn(mock(UserEntity.class));
        when(userDetailRepository.saveAndFlush(any())).thenReturn(mock(UserDetailEntity.class));

        assertThat(throwUserApplicationException())
                .isInstanceOf(UserApplicationException.class)
                .hasMessage("is exist user.");
    }

    private UserRegisterRequest createUserRegisterInfo() {
        return UserRegisterRequest.builder()
//                .userType(UserType.REGULAR_MEMBER)
                .userAccount("aebong@gmail.com")
//                .userAccountType(UserAccountType.EMAIL)
                .userPassword("nonencodepassword")
//                .userStatus(UserStatus.ACTIVATED)
//                .requiredPasswordChangeDatetime(LocalDateTime.now().plusDays(90))
                .firstName("애봉")
                .lastName("이")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
//                .email("aebong@gmail.com")
                .address1("대구광역시 동구 동대구로 503")
                .zipcode("42176")
                .build();
    }

    private void userApplicationException() {
        throw new UserApplicationException(CustomErrorType.IS_EXIST_USER, CustomErrorType.IS_EXIST_USER.getMessage());
    }

    private Throwable throwUserApplicationException() {
        return catchThrowable(this::userApplicationException);
    }

}