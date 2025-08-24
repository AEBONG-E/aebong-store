package com.aebong.store.service.user;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.exceptions.UserApplicationException;
import com.aebong.store.domain.repository.user.UserDetailRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import com.aebong.store.service.user.dto.UserRegisterInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    @Override
    public void registerUser(UserRegisterInfo registerInfo) {

        if (Objects.isNull(registerInfo)) {
            throw new IllegalArgumentException("registerInfo must not be null");
        }

        // 회원 계정 중복 검증
        if (validateUserAccountIsExists(registerInfo.getUserAccount())) {
            throw new UserApplicationException(CustomErrorType.IS_EXIST_USER,
                    String.format("%s already exists userAccount", registerInfo.getUserAccount()));
        }

        // 회원 DB 저장 데이터 생성 (todo: 회원 기본 정보 DB 저장 전 비밀번호 암호화 처리 필요)
        userRepository.save(registerInfo.toUserEntity());
        userDetailRepository.save(registerInfo.toUserDetailEntity(registerInfo.toUserEntity()));

    }

    private boolean validateUserAccountIsExists(String userAccount) {
        if (Objects.isNull(userAccount)) {
            throw new IllegalArgumentException("userAccount is null");
        }
        return userRepository.existsByUserAccount(userAccount);
    }

}
