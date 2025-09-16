package com.aebong.store.service.user;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.common.exceptions.UserApplicationException;
import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.domain.entity.user.UserDetailEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.UserDetailRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import com.aebong.store.service.user.dto.UserReadInfo;
import com.aebong.store.service.user.dto.UserRegisterInfo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired private UserService service;
    @MockBean private UserRepository userRepository;
    @MockBean private UserDetailRepository userDetailRepository;


    @Test
    void 회원등록_실패_중복된_userAccount가_들어온_케이스() {

        // given
        UserRegisterRequest request = createUserRegisterInfo();
        UserRegisterInfo registerInfo = UserRegisterInfo.to(request);
        UserEntity user = registerInfo.toUserEntity();
        UserDetailEntity userDetail = registerInfo.toUserDetailEntity(user);

        String validateUserAccount = "aebong@gmail.com";

        given(userRepository.existsByUserAccount(validateUserAccount)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> service.registerUser(request))
                                        .isInstanceOf(UserApplicationException.class)
                                        .hasMessage(validateUserAccount + " already exists userAccount");

        then(userRepository).should(times(1)).existsByUserAccount(validateUserAccount);
        then(userRepository).should(never()).save(any());
        then(userDetailRepository).shouldHaveNoInteractions(); // not running

    }

    @Test
    void 회원등록_정상_케이스() {

        // given
        UserRegisterRequest request = createUserRegisterInfo();
        UserRegisterInfo registerInfo = UserRegisterInfo.to(request);
        UserEntity user = registerInfo.toUserEntity();
        UserDetailEntity userDetail = registerInfo.toUserDetailEntity(user);

        String validateUserAccount = "test@gmail.com";

        given(userRepository.existsByUserAccount(validateUserAccount)).willReturn(false);
        given(userRepository.save(user)).willReturn(user);
        given(userDetailRepository.save(userDetail)).willReturn(userDetail);

        // when
        service.registerUser(request);

        // then
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        ArgumentCaptor<UserDetailEntity> userDetailCaptor = ArgumentCaptor.forClass(UserDetailEntity.class);

        then(userRepository).should(times(1)).save(userCaptor.capture());
        then(userDetailRepository).should(times(1)).save(userDetailCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();
        UserDetailEntity savedUserDetail = userDetailCaptor.getValue();

        assertThat(savedUser.getUserAccount()).isEqualTo("aebong@gmail.com");

        assertThat(savedUserDetail.getLastName()).isEqualTo("ae");
        assertThat(savedUserDetail.getFirstName()).isEqualTo("bong");
        assertThat(savedUserDetail.getGender()).isEqualTo(Gender.MALE);
        assertThat(savedUserDetail.getAddress().getAddress1()).isEqualTo("테스트시 테스트구 테스트로 1");
        assertThat(savedUserDetail.getAddress().getZipcode()).isEqualTo("00000");

    }

    @Test
    void 회원조회_실패_등록되지_않은_userAccount가_들어온_케이스() {

        // given
        UserRegisterRequest request = createUserRegisterInfo();
        UserRegisterInfo registerInfo = UserRegisterInfo.to(request);
        UserEntity user = registerInfo.toUserEntity();
        UserDetailEntity userDetail = registerInfo.toUserDetailEntity(user);

        given(userRepository.save(user)).willReturn(user);
        given(userDetailRepository.save(userDetail)).willReturn(userDetail);

        String requestUserAccount = "test@gmail.com";
        given(userRepository.findByUserAccount(requestUserAccount)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.getUser(requestUserAccount))
                                        .isInstanceOf(UserApplicationException.class)
                                        .hasMessage(CustomErrorType.NOT_FOUND_USER.getMessage());

        then(userRepository).should(times(1)).findByUserAccount(requestUserAccount);
        then(userDetailRepository).shouldHaveNoInteractions();
    }

    @Test
    void 회원조회_정상_케이스() {

        // given
        UserRegisterRequest request = createUserRegisterInfo();
        UserRegisterInfo registerInfo = UserRegisterInfo.to(request);
        UserEntity user = registerInfo.toUserEntity();
        UserDetailEntity userDetail = registerInfo.toUserDetailEntity(user);

        String requestUserAccount = "aebong@gmail.com";

        given(userRepository.save(any())).willReturn(UserEntity.class);
        given(userDetailRepository.save(any())).willReturn(UserDetailEntity.class);
        given(userRepository.findByUserAccount(requestUserAccount)).willReturn(Optional.of(user));
        given(userDetailRepository.findByUser(user)).willReturn(Optional.of(userDetail));

        // when
        UserReadInfo readInfo = service.getUser(requestUserAccount);

        // then
        assertThat(readInfo).isNotNull();
        assertThat(readInfo.getUserAccount()).isEqualTo(requestUserAccount);

        assertThat(readInfo)
                .hasFieldOrPropertyWithValue("userAccount", user.getUserAccount())
                .hasFieldOrPropertyWithValue("userPassword", user.getUserPassword())
                .hasFieldOrPropertyWithValue("lastName", userDetail.getLastName())
                .hasFieldOrPropertyWithValue("firstName", userDetail.getFirstName())
                .hasFieldOrPropertyWithValue("birthDate", userDetail.getBirthDate())
                .hasFieldOrPropertyWithValue("gender", userDetail.getGender())
                .hasFieldOrPropertyWithValue("address", userDetail.getAddress());

        then(userRepository).should().findByUserAccount(requestUserAccount);
        then(userDetailRepository).should().findByUser(user);
    }

    private UserRegisterRequest createUserRegisterInfo() {
        return UserRegisterRequest.builder()
                .userAccount("aebong@gmail.com")
                .userPassword("nonencodepassword")
                .lastName("ae")
                .firstName("bong")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .address1("테스트시 테스트구 테스트로 1")
                .zipcode("00000")
                .build();
    }

    private UserReadInfo createUserReadInfo() {
        return UserReadInfo.builder()
                .build();
    }

    private void userApplicationException() {
        throw new UserApplicationException(CustomErrorType.IS_EXIST_USER, CustomErrorType.IS_EXIST_USER.getMessage());
    }

    private Throwable throwUserApplicationException() {
        return catchThrowable(this::userApplicationException);
    }

}