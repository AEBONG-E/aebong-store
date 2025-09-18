package com.aebong.store.service.user;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.exceptions.UserApplicationException;
import com.aebong.store.controller.req.UserModifyRequest;
import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.domain.entity.user.UserDetailEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.UserDetailRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import com.aebong.store.service.user.dto.UserGetInfo;
import com.aebong.store.service.user.dto.UserModifyInfo;
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
    void 사용자등록_실패_중복된_userAccount가_들어온_케이스() {

        // given
        UserRegisterRequest request = createUserRegisterInfo();
        UserRegisterInfo registerInfo = UserRegisterInfo.to(request);
        UserEntity user = registerInfo.toUserEntity();
        UserDetailEntity userDetail = registerInfo.toUserDetailEntity(user);

        String validateUserAccount = "aebong@gmail.com";

        given(userRepository.existsByUserAccount(validateUserAccount)).willReturn(true);

        // when
        assertThatThrownBy(() -> service.registerUser(request))
                                        .isInstanceOf(UserApplicationException.class)
                                        .hasMessage(validateUserAccount + " already exists userAccount");

        // then
        then(userRepository).should(times(1)).existsByUserAccount(validateUserAccount);
        then(userRepository).should(never()).save(any());
        then(userDetailRepository).shouldHaveNoInteractions(); // not running

    }

    @Test
    void 사용자등록_정상_케이스() {

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
    void 사용자조회_실패_등록된_사용자를_찾을수없는_케이스() {

        // given
        UserRegisterRequest request = createUserRegisterInfo();
        UserRegisterInfo registerInfo = UserRegisterInfo.to(request);
        UserEntity user = registerInfo.toUserEntity();
        UserDetailEntity userDetail = registerInfo.toUserDetailEntity(user);

        given(userRepository.save(user)).willReturn(user);
        given(userDetailRepository.save(userDetail)).willReturn(userDetail);

        String requestUserAccount = "test@gmail.com";
        given(userRepository.findByUserAccount(requestUserAccount)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> service.getUser(requestUserAccount))
                                        .isInstanceOf(UserApplicationException.class)
                                        .hasMessage(CustomErrorType.NOT_FOUND_USER.getMessage());

        // then
        then(userRepository).should(times(1)).findByUserAccount(requestUserAccount);
        then(userDetailRepository).shouldHaveNoInteractions();
    }

    @Test
    void 사용자조회_정상_케이스() {

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
        UserGetInfo readInfo = service.getUser(requestUserAccount);

        // then
        assertThat(readInfo).isNotNull();
        assertThat(readInfo.getUserAccount()).isEqualTo(requestUserAccount);

        assertThat(readInfo)
                .hasFieldOrPropertyWithValue("userType", user.getUserType())
                .hasFieldOrPropertyWithValue("userAccount", user.getUserAccount())
                .hasFieldOrPropertyWithValue("userAccountType", user.getUserAccountType())
                .hasFieldOrPropertyWithValue("userPassword", user.getUserPassword())
                .hasFieldOrPropertyWithValue("lastName", userDetail.getLastName())
                .hasFieldOrPropertyWithValue("firstName", userDetail.getFirstName())
                .hasFieldOrPropertyWithValue("birthDate", userDetail.getBirthDate())
                .hasFieldOrPropertyWithValue("gender", userDetail.getGender())
                .hasFieldOrPropertyWithValue("mobileNumber", userDetail.getMobileNumber())
                .hasFieldOrPropertyWithValue("nickName", userDetail.getNickName())
                .hasFieldOrPropertyWithValue("address", userDetail.getAddress());

        then(userRepository).should().findByUserAccount(requestUserAccount);
        then(userDetailRepository).should().findByUser(user);
    }

    @Test
    void 사용자수정_실패_등록된_사용자를_찾을수없는_케이스() {

        // given
        UserModifyRequest request = createUserModifyInfo();
        UserModifyInfo registerInfo = UserModifyInfo.to(request);

        given(userRepository.findById(registerInfo.getUserId())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> service.modifyUser(request))
                .isInstanceOf(UserApplicationException.class)
                .hasMessage(CustomErrorType.NOT_FOUND_USER.getMessage());

        // then
        then(userRepository).should(times(1)).findById(registerInfo.getUserId());
        then(userDetailRepository).shouldHaveNoInteractions();
    }

    @Test
    void 사용자수정_정상_케이스() {

        // given
        UserModifyRequest request = createUserModifyInfo();
        UserModifyInfo modifyInfo = UserModifyInfo.to(request);

        UserRegisterRequest registerRequest = createUserRegisterInfo();
        UserRegisterInfo registerInfo = UserRegisterInfo.to(registerRequest);

        UserEntity user = registerInfo.toUserEntity();
        UserDetailEntity userDetail = registerInfo.toUserDetailEntity(user);

        given(userRepository.findById(modifyInfo.getUserId())).willReturn(Optional.of(user));
        given(userDetailRepository.findByUser(user)).willReturn(Optional.of(userDetail));

        // when
        service.modifyUser(request);

        // then
        then(userRepository).should(times(1)).findById(modifyInfo.getUserId());
        then(userDetailRepository).should(times(1)).findByUser(user);

        // check dirty checking
        assertThat(userDetail.getMobileNumber()).isNotEqualTo(registerInfo.getMobileNumber());
        assertThat(userDetail.getNickName()).isNotEqualTo(registerInfo.getNickName());
        assertThat(userDetail.getAddress().getAddress1()).isNotEqualTo(registerInfo.getAddress().getAddress1());
        assertThat(userDetail.getAddress().getZipcode()).isNotEqualTo(registerInfo.getAddress().getZipcode());

    }

    @Test
    void 사용자삭제_실패_등록된_사용자를_찾을수없는_케이스() {

        // given
        UserRegisterRequest request = createUserRegisterInfo();
        UserRegisterInfo registerInfo = UserRegisterInfo.to(request);
        UserEntity user = registerInfo.toUserEntity();
        UserDetailEntity userDetail = registerInfo.toUserDetailEntity(user);

        given(userRepository.save(user)).willReturn(user);
        given(userDetailRepository.save(userDetail)).willReturn(userDetail);

        String requestUserAccount = "test@gmail.com";
        given(userRepository.findByUserAccount(requestUserAccount)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> service.deleteUser(requestUserAccount))
                .isInstanceOf(UserApplicationException.class)
                .hasMessage(CustomErrorType.NOT_FOUND_USER.getMessage());

        // then
        then(userRepository).should(times(1)).findByUserAccount(requestUserAccount);
        then(userDetailRepository).shouldHaveNoInteractions();
    }

    @Test
    void 사용자삭제_정상_케이스() {

        // given
        UserRegisterRequest request = createUserRegisterInfo();
        UserRegisterInfo registerInfo = UserRegisterInfo.to(request);
        UserEntity user = registerInfo.toUserEntity();
        UserDetailEntity userDetail = registerInfo.toUserDetailEntity(user);

        given(userRepository.save(user)).willReturn(user);
        given(userDetailRepository.save(userDetail)).willReturn(userDetail);

        String requestUserAccount = "aebong@gmail.com";
        given(userRepository.findByUserAccount(requestUserAccount)).willReturn(Optional.of(user));
        given(userDetailRepository.findByUser(user)).willReturn(Optional.of(userDetail));

        // when
        service.deleteUser(requestUserAccount);

        // then
        then(userRepository).should(times(1)).findByUserAccount(requestUserAccount);
        then(userDetailRepository).should(times(1)).findByUser(user);

        // check dirty checking
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.WITHDRAWAL);
        assertThat(user.getIsDeleted()).isEqualTo(Boolean.TRUE);
        assertThat(userDetail.getWithdrawalDatetime()).isNotNull();
        assertThat(userDetail.getIsDeleted()).isEqualTo(Boolean.TRUE);

    }

    private UserRegisterRequest createUserRegisterInfo() {
        return UserRegisterRequest.builder()
                .userAccount("aebong@gmail.com")
                .userPassword("nonencodepassword")
                .lastName("ae")
                .firstName("bong")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .mobileNumber("01011111234")
                .nickName("aebong")
                .address1("테스트시 테스트구 테스트로 1")
                .address2("테스트")
                .zipcode("00000")
                .build();
    }

    private UserModifyRequest createUserModifyInfo() {
        return UserModifyRequest.builder()
                .userId(0L)
                .userDetailId(0L)
                .userAccount("aebong@gmail.com")
                .userPassword("nonencodepassword")
                .lastName("ae")
                .firstName("bong")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .mobileNumber("01022221234")
                .nickName("aebong1")
                .address1("테스트시 테스트구 테스트로 2")
                .address2("테스트")
                .zipcode("00001")
                .build();
    }

    private void userApplicationException() {
        throw new UserApplicationException(CustomErrorType.IS_EXIST_USER, CustomErrorType.IS_EXIST_USER.getMessage());
    }

    private Throwable throwUserApplicationException() {
        return catchThrowable(this::userApplicationException);
    }

}