package com.aebong.store.service.user;

import com.aebong.store.common.exceptions.UserInvalidException;
import com.aebong.store.domain.entity.user.UserDetailEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.UserDetailRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import com.aebong.store.service.user.dto.UserRegisterInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;

    /**
     * 회원 등록
     * @param registerInfo 회원 등록 정보(api 통해 request 로 부터 받아올 매개변수)
     */
    @Override
    public void registerUser(UserRegisterInfo registerInfo) {

        if (Objects.isNull(registerInfo)) {
            throw new IllegalArgumentException("registerInfo must not be null");
        }

        // 회원 계정 중복 검증 (todo: 추후 예외 코드 및 상태 코드 추가 시 리팩토링 필요)
        if (validateUserAccountIsExists(registerInfo.getUserAccount())) {
            throw new UserInvalidException("Invalid user data.");
        }

        // 회원 DB 저장 데이터 생성 (todo: 회원 기본 정보 DB 저장 전 비밀번호 암호화 처리 필요)
        UserEntity userBasicInfo = registerInfo.toUserEntity();
        this.userRepository.save(userBasicInfo);

        UserDetailEntity userDetail = registerInfo.toUserDetailEntity(userBasicInfo);
        this.userDetailRepository.save(userDetail);

    }

    private boolean validateUserAccountIsExists(String userAccount) {
        if (Objects.isNull(userAccount)) {
            throw new IllegalArgumentException("userAccount is null");
        }
        return this.userRepository.existsByUserAccount(userAccount);
    }

}
