package com.aebong.store.service.user;

import com.aebong.store.annotation.UnitTest;
import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.common.exceptions.UserInvalidException;
import com.aebong.store.domain.entity.user.UserDetailEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.UserDetailRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import com.aebong.store.service.user.dto.UserRegisterInfo;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@UnitTest
class UserRegisterServiceUnitTest {
    
    @Autowired private UserRepository userRepository;
    @Autowired private UserDetailRepository userDetailRepository;
    @Autowired private EntityManager em;

    @DisplayName("[Success]-회원 등록 시 유효한 사용자 정보가 주어지면 정상적으로 사용자 정보가 저장되어야 한다.")
    @Test
    void givenValidUserRegisterInfo_whenSaving_thenUserIsPersisted() {
        // given
        UserRegisterInfo registerInfo = createCommonUserRegisterInfo();
        UserEntity newUser = registerInfo.toUserEntity();
        UserDetailEntity newUserDetail = registerInfo.toUserDetailEntity(newUser);

        // when
        this.userRepository.save(newUser);
        this.userDetailRepository.save(newUserDetail);

        em.flush();
        em.clear();

        boolean isEqualsUserAccount = this.userRepository.existsByUserAccount(newUser.getUserAccount());
        boolean isEqualsEmail = this.userDetailRepository.existsByEmail(newUserDetail.getEmail());

        Optional<UserEntity> findUserInfo = this.userRepository.findById(newUser.getId());

        // then
        assertThat(isEqualsUserAccount).isTrue();
        assertThat(isEqualsEmail).isTrue();
        assertThat(findUserInfo.get().getUserAccount()).isEqualTo("aebong@gmail.com");

    }

    @DisplayName("[Exception]-회원 등록 시 중복된 회원 계정 사용자 정보가 주어지면 예외를 반환한다")
    @Test
    void givenExistingUserAccount_whenSaving_thenThrowsUserInvalidException() {
        // given
        UserRegisterInfo registerInfo = createCommonUserRegisterInfo();
        UserEntity user1 = registerInfo.toUserEntity();
        UserDetailEntity userDetail1 = registerInfo.toUserDetailEntity(user1);

        UserRegisterInfo registerInfo2 = UserRegisterInfo.builder()
                .userType(UserType.REGULAR_MEMBER)
                .userAccount("aebong@gmail.com")
                .userAccountType(UserAccountType.EMAIL)
                .userPassword("nonencodepassword")
                .userStatus(UserStatus.ACTIVATED)
                .requiredPasswordChangeDatetime(LocalDateTime.now().plusDays(90))
                .firstName("Aebong")
                .lastName("E")
                .gender(Gender.MALE)
                .email("aebonge@gmail.com")
                .address1("대구광역시 수성구 동대구로 86")
                .zipcode("42176")
                .build();
        UserEntity user2 = registerInfo2.toUserEntity();
        UserDetailEntity userDetail2 = registerInfo.toUserDetailEntity(user2);

        // when
        this.userRepository.save(user1);
        this.userDetailRepository.save(userDetail1);

        em.flush();
        em.clear();

        boolean isEqualsUserAccount = this.userRepository.existsByUserAccount(user2.getUserAccount());
        boolean isEqualsEmail = this.userDetailRepository.existsByEmail(userDetail2.getEmail());

        // then
        assertThat(isEqualsUserAccount).isTrue();
        assertThat(isEqualsEmail).isTrue();
        assertThatThrownBy(() -> userIsExists(user2.getUserAccount()))
                .isInstanceOf(UserInvalidException.class).hasMessage("Invalid user data.");

    }

    private UserRegisterInfo createCommonUserRegisterInfo() {
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

    private void userIsExists(String userAccount) {
        if (Objects.nonNull(userAccount)) {
            boolean isExists = this.userRepository.existsByUserAccount(userAccount);
            if (isExists) {
                throw new UserInvalidException("Invalid user data.");
            }
        }
    }

}